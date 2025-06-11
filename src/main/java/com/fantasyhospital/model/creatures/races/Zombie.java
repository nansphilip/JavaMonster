package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.TriageResident;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.model.creatures.interfaces.Regenerating;
import com.fantasyhospital.model.rooms.Room;
import com.fantasyhospital.model.rooms.medicalservice.Crypt;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j @Getter
public class Zombie extends TriageResident implements Regenerating {

    /**
     * True if the vampire has already regenerate once, false otherwise (particular rule of the crypt)
     */
    public boolean hasRegenerate = false;

    public Zombie() {
        super(null);
    }

    public Zombie(CopyOnWriteArrayList<Disease> diseases) {
        super(diseases);
    }

    @Override
    public boolean die(Room room) {
        if(hasRegenerate && room instanceof Crypt) {
            log.info("La créature {} avait déjà regénéré une fois dans la crypte, elle n'a malheureusement pas de seconde chance...tchao",  this.fullName);
            return true;
        }
        log.info("La créature {} meurt...", this.fullName);
        hasRegenerate = true;
        return regenerate(this);
    }
}