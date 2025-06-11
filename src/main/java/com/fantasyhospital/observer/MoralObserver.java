package com.fantasyhospital.observer;

import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Beast;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.rooms.Room;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import lombok.extern.slf4j.Slf4j;

import static com.fantasyhospital.model.creatures.abstractclass.BeastUtils.setNameAvailableAgain;

/**
 * Implementation of the Observer interface that monitors the morale of creatures
 * The notifier will execute potential actions that would result from a morale of 0 or other conditions
 */
@Slf4j
public class MoralObserver implements CreatureObserver {

    /**
     * The hospital to which this observer is attached
     */
    private Hospital hospital;

    // Constants of variation
    public static final int DECREASE_BUDGET = 8;

    /**
     * Constructor for the MoralObserver
     * @param hospital The hospital to which this observer is attached
     */
    public MoralObserver(Hospital hospital) {
        this.hospital = hospital;
    }

    /**
     * Implementation of the onStateChanged method. It calls the checkMorale method on the doctor, that checks if its moral is not at 0, and executes the method if it is
     * Checks the creature moral with the checkMorale method, executes potential actions and removes the creature from the hospital if it has passed away
     * @param beast the beast whose state has changed
     */
    @Override
    public void onStateChanged(Beast beast) {
        if(beast instanceof Doctor){
            ((Doctor) beast).checkMorale();
            return;
        }
        Creature creature = (Creature) beast;
        Room room = hospital.getRoomOfCreature(creature);
        if(creature.checkMorale(room)){
            // Make the name of the creature available again
            setNameAvailableAgain(creature);

            room.removeCreature(creature);
            if(room instanceof MedicalService){
                Doctor doc = ((MedicalService) room).getWeakerDoctor();
                if(doc != null) doc.depression();
            }
            // Decrease the budget of the service if it was in a medical service
            if(room instanceof MedicalService medicalService){
                medicalService.setBudget(Math.max(medicalService.getBudget() - DECREASE_BUDGET,0));
                log.info("La mort de la créature {} fait perdre {} points de budget au service {} ({} pts)", creature.getFullName(), DECREASE_BUDGET, medicalService.getName(), medicalService.getBudget());
            }
        }
    }
}
