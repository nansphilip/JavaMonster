package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.TriageResident;
import com.fantasyhospital.model.creatures.interfaces.Contaminant;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.model.rooms.Room;

import java.util.concurrent.CopyOnWriteArrayList;

public class Lycanthrope extends TriageResident implements Contaminant {

    public Lycanthrope() {
        this( null);
    }

    public Lycanthrope(CopyOnWriteArrayList<Disease> diseases) {
        super(diseases);
    }

    @Override
    public boolean die(Room room) {
        super.die(room);
        contaminate(this, room);
        return true;
    }
}