package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.VIPPatient;
import com.fantasyhospital.model.creatures.interfaces.Demoralizing;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.model.rooms.Room;

import java.util.concurrent.CopyOnWriteArrayList;

public class Elf extends VIPPatient implements Demoralizing {

    public Elf() {
        super(null);
    }

    public Elf(CopyOnWriteArrayList<Disease> diseases) {
        super(diseases);
    }

    @Override
    public boolean die(Room room) {
        super.die(room);
        demoralize(this, room);
        return true;
    }
}
