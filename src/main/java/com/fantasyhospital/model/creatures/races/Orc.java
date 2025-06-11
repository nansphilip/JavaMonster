package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.TriageResident;
import com.fantasyhospital.model.creatures.interfaces.Contaminant;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.model.rooms.Room;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class Orc extends TriageResident implements Contaminant {

    public Orc() {
        super(null);
    }

    public Orc(CopyOnWriteArrayList<Disease> diseases) {
        super(diseases);
    }

    @Override
    public boolean die(Room room) {
        log.info("La créature {} meurt...", this.fullName);
        contaminate(this, room);
        return true;
    }
}