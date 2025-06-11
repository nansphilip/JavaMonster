package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.TriageResident;
import com.fantasyhospital.model.creatures.interfaces.Contaminant;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.model.rooms.Room;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class Werebeast extends TriageResident implements Contaminant {

    public Werebeast() {
        super(null);
    }

    public Werebeast(CopyOnWriteArrayList<Disease> diseases) {
        super(diseases);
    }

    @Override
    public boolean die(Room room) {
        log.info("La créature {} meurt...", this.fullName);
        contaminate(this, room);
        return true;
    }
}