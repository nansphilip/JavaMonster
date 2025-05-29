package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.TriageResident;
import com.fantasyhospital.model.creatures.interfaces.Contaminant;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.rooms.Room;

import java.util.concurrent.CopyOnWriteArrayList;

public class Orc extends TriageResident implements Contaminant {

    public Orc() {
        super(null);
    }

    public Orc(CopyOnWriteArrayList<Disease> diseases) {
        super(diseases);
    }

//    public Orque(String nom, String sexe, int poids, int taille, int age, int moral, HashSet<Disease> diseases) {
//        super(nom, sexe, poids, taille, age, moral, diseases);
//    }

    @Override
    public boolean die(Room room) {
        super.die(room);
        contaminate(this, room);
        return true;
    }
}