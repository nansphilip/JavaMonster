package com.fantasyhospital.observer;

import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Medecin;
import com.fantasyhospital.model.creatures.abstractclass.Bete;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.salles.Salle;
import com.fantasyhospital.salles.servicemedical.ServiceMedical;
import lombok.extern.slf4j.Slf4j;

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
    public void onStateChanged(Bete bete) {
        checkExitCreature((Creature) bete);
    }


    /**
     * Méthode qui vérifie si une créature doit sortir de l'hopital
     * Elle appelle la méthode hasCreatureToLeaveHospital, cette dernière retourne true si c'est le cas
     * Elle applique également depression sur le médecin le plus faible si la créature a trépassé
     * @param creature
     */
    private void checkExitCreature(Creature creature) {
        Salle salleCreature = this.hospital.getSalleOfCreature(creature);
        //Si la créature est déjà sortie de l'hopital
        if(salleCreature == null){
            return;
        }

        boolean getsOut = creature.hasCreatureToleaveHospital(salleCreature);

        //Si creature meurt, médecin le plus faible du service perd du moral
        if(getsOut){
            //On vérifie que la créature a bien trépassé, c'est à dire qu'elle a des maladies
            if(!creature.getMaladies().isEmpty()){
                if(salleCreature instanceof ServiceMedical){
                    Medecin medecin = ((ServiceMedical) salleCreature).getWeakerMedecin();
                    if(medecin != null){
                        medecin.depression();
                        //On notifie l'observer du médecin pour vérifier si il n'en finit pas
                        medecin.notifyObservers();
                    }
                }
            }
            salleCreature.enleverCreature(creature);
        }
    }

}
