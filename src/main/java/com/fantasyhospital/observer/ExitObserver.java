package com.fantasyhospital.observer;

import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Beast;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.interfaces.Regenerating;
import com.fantasyhospital.model.creatures.races.Vampire;
import com.fantasyhospital.model.creatures.races.Zombie;
import com.fantasyhospital.rooms.Room;
import com.fantasyhospital.rooms.medicalservice.MedicalService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Implémentation de l'interface Observer chargé de surveiller si une bête doit sortir de l'hopital
 * Soit parce qu'elle est soignée et n'a plus de maladie, soit parce qu'elle a trepassé
 */
@Slf4j
public class ExitObserver implements CreatureObserver {

    /**
     * Attribut qui référence l'hospital pour surveiller une bête, et pouvoir la faire sortir de l'hospital
     */
    private Hospital hospital;

    public ExitObserver(Hospital hospital) {
        this.hospital = hospital;
    }

    /**
     * Implémentation de la méthode onStateChanged
     * Elle appelle la méthode checkExitCreature
     * @param bete
     */
    @Override
    public void onStateChanged(Beast beast) {
        checkExitCreature((Creature) beast);
    }


    /**
     * Méthode qui vérifie si une créature doit sortir de l'hopital
     * Elle appelle la méthode hasCreatureToLeaveHospital, cette dernière retourne true si c'est le cas
     * Elle applique également depression sur le médecin le plus faible si la créature a trépassé
     * @param creature
     */
    private void checkExitCreature(Creature creature) {
        Room salleCreature = this.hospital.getRoomOfCreature(creature);
        //Si la créature est déjà sortie de l'hopital
        if(salleCreature == null){
            return;
        }

        //Avant de potentiellement faire trepasser la creature, si regenerant, on check si creature va mourir
        //Si va mourir, on appliquera depression sur medecin
        boolean isDead = false;
        if(Regenerating.class.isAssignableFrom(creature.getClass())){
            if(creature instanceof Zombie zombie){
                isDead = zombie.isCreatureDeadButWillRevive(creature);
            } else if (creature instanceof Vampire vampire){
                isDead = vampire.isCreatureDeadButWillRevive(creature);
            } else {
                throw new RuntimeException("Unrecognized creature class: " + creature.getClass().getName());
            }
        }

        boolean getsOut = creature.hasCreatureToleaveHospital(salleCreature);

        //Si creature meurt, médecin le plus faible du service perd du moral
        if(getsOut){
            //On vérifie que la créature a bien trépassé et non soignée, c'est à dire qu'elle a des maladies
            if(!creature.getDiseases().isEmpty()){
                if(salleCreature instanceof MedicalService){
                    Doctor doctor = ((MedicalService) salleCreature).getWeakerDoctor();
                    if(doctor != null){
                        doctor.depression();
                    } else {
                        log.info("Il n'y avait aucun médecin à déprimer dans le service.");
                    }
                }
            }
            salleCreature.removeCreature(creature);
        }

        //Si regenerant qui meurt mais reste quand même dans l'hopital après avoir regénéré, applique depression a medecin
        if(isDead){
            if(salleCreature instanceof MedicalService){
                Doctor doctor = ((MedicalService) salleCreature).getWeakerDoctor();
                if(doctor != null) doctor.depression();
            }
        }
    }

}
