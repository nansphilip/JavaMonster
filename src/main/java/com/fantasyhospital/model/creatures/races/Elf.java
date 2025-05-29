package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.VIPPatient;
import com.fantasyhospital.model.creatures.interfaces.Demoralizing;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.rooms.Room;
import java.util.concurrent.CopyOnWriteArrayList;


public class Elf extends VIPPatient implements Demoralizing {

    public Elf() {
        super(null);
    }

    public Elf(CopyOnWriteArrayList<Disease> diseases) {
        super(diseases);
    }

//    public Elfe(String nomComplet, String sexe, int poids, int taille, int age, int moral, HashSet<Disease> diseases) {
//        super(nomComplet, sexe, poids, taille, age, moral, diseases);
//    }

    @Override
    public boolean die(Room room) {
        super.die(room);
        demoralize(this, room);
        return true;
    }
}
