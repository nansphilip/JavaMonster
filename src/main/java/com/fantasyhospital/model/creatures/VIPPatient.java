package com.fantasyhospital.model.creatures;

import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.model.rooms.Room;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public abstract class VIPPatient extends Creature {
    private int waitingRounds;

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
        } else {
            //log.info("La créature {} attend.", this.fullName);
        }
        notifyMoralObservers();
    }

    @Override
    public boolean beCured(Disease disease) {
        this.waitingRounds = 0;
        return super.beCured(disease);
    }
}
