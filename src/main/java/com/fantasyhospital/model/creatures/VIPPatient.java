package com.fantasyhospital.model.creatures;

import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.model.rooms.Room;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Abstract class representing the VIP patient in the hospital.
 * A VIP patient doesn't wait well, so if it waits 4 tours without being cared of its morale goes down to 0
 *
 */
@Slf4j
public abstract class VIPPatient extends Creature {
    public int waitingRounds;

    public VIPPatient( CopyOnWriteArrayList<Disease> diseases) {
        super(diseases);
    }

    @Override
    public void waiting(Room room) {
        //moral tombe 0 au bout de 4 tours
        this.waitingRounds++;
        if(this.waitingRounds == 4){
            this.morale = 0;
            this.waitingRounds = 0;
            log.info("La créature {} a attendu 4 tours sans être soigné, son moral tombe à 0.", this.fullName);
        }
        notifyMoralObservers();
    }

    /**
     * Override the beCured method to reset the waitingRounds when the VIP patient is cured (in case of it has several diseases).
     * @param disease the disease being cured
     * @param medicalService the medical service providing the cure
     * @return true if the cure was successful, false otherwise
     */
    @Override
    public boolean beCured(Disease disease, MedicalService medicalService) {
        this.waitingRounds = 0;
        return super.beCured(disease, medicalService);
    }
}
