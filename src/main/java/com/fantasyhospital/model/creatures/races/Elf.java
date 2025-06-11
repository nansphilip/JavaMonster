package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.VIPPatient;
import com.fantasyhospital.model.creatures.interfaces.Demoralizing;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.model.rooms.Room;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class Elf extends VIPPatient implements Demoralizing {

    public Elf() {
        super(null);
    }

    public Elf(CopyOnWriteArrayList<Disease> diseases) {
        super(diseases);
    }

    @Override
    public boolean die(Room room) {
        log.info("La créature {} meurt...", this.fullName);
        demoralize(this, room);
        return true;
    }
}
