package com.fantasyhospital.observer;

import com.fantasyhospital.enums.FemaleNameType;
import com.fantasyhospital.enums.MaleNameType;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Beast;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.interfaces.Regenerating;
import com.fantasyhospital.model.creatures.races.Vampire;
import com.fantasyhospital.model.creatures.races.Zombie;
import com.fantasyhospital.model.rooms.Room;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

import static com.fantasyhospital.model.creatures.abstractclass.BeastUtils.setNameAvailableAgain;
import static com.fantasyhospital.observer.MoralObserver.DECREASE_BUDGET;

/**
 * Implementation of the Observer interface that monitors whether a beast should leave the hospital
 * Either because it has been totally treated and no longer has any diseases, or because it has passed away.
 */
@Slf4j
public class ExitObserver implements CreatureObserver {

    /**
     * The hospital to which this observer is attached
     */
    private final Hospital hospital;

    /**
     * Set of creatures that have already been processed to avoid double processing
     * This is useful to prevent the same creature from being processed multiple times (infinite loop), especially for contaminating or regenerating creatures
     */
    private Set<Creature> processedCreatures = new HashSet<>();

    /**
     * Constructor for the ExitObserver
     * @param hospital The hospital to which this observer is attached
     */
    public ExitObserver(Hospital hospital) {
        this.hospital = hospital;
    }

    /**
     * Implementation of the onStateChanged method, that calls the checkExitCreature method
     * @param beast The beast whose state has changed
     */
    @Override
    public void onStateChanged(Beast beast) {
        checkExitCreature((Creature) beast);
    }


    /**
     * Checks if the creature should leave the hospital. If the creature is a regenerating type, the depression is applied to the doctor
     * even if the creature doesn't die effectively.
     * @param creature The creature to check
     */
    private void checkExitCreature(Creature creature) {
        Room salleCreature = this.hospital.getRoomOfCreature(creature);
        if(salleCreature == null){
            return;
        }

        // Ajout d'une vérification pour voir si on a déjà traité la créature courante dans ce tour (pour éviter loop infinie)
        if(processedCreatures.contains(creature)) {
            return;
        }

        //Avant de potentiellement faire trepasser la creature, si c'est un regenerant, on check si creature va mourir
        //Si ce check était fait après, la créature n'aurait possiblement plus la maladie lethale ou autre (regenerant)
        //Si va mourir, on applique depression sur medecin
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

        boolean getsOut = false;
        try {
            processedCreatures.add(creature);

            getsOut = creature.hasCreatureToleaveHospital(salleCreature);

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
                    // Decrease the budget of the service if it was in a medical service
                    if(salleCreature instanceof MedicalService medicalService){
                        medicalService.setBudget(Math.max(medicalService.getBudget() - DECREASE_BUDGET,0));
                        log.info("La mort de la créature {} fait perdre {} points de budget au service {} ({} pts)", creature.getFullName(), DECREASE_BUDGET, medicalService.getName(), medicalService.getBudget());
                    }
                }
                // Make the name of the creature available again
                setNameAvailableAgain(creature);

                salleCreature.removeCreature(creature);
            }
        } finally {
            processedCreatures.remove(creature);
        }

        //Si regenerant qui meurt mais reste quand même dans l'hopital après avoir regénéré, applique depression a medecin
        if(isDead && !getsOut){
            if(salleCreature instanceof MedicalService){
                Doctor doctor = ((MedicalService) salleCreature).getWeakerDoctor();
                if(doctor != null) doctor.depression();
            }
        }
    }

}
