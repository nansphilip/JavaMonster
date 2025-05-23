package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.TriageResident;
import com.fantasyhospital.model.creatures.interfaces.Contaminant;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.rooms.Room;

import java.util.concurrent.CopyOnWriteArrayList;

public class Werebeast extends TriageResident implements Contaminant {

    public Werebeast() {
        super(null);
    }

    public Werebeast(CopyOnWriteArrayList<Disease> diseases) {
        super(diseases);
    }
//
//    public HommeBete(String nom, String sexe, int poids, int taille, int age, int moral, HashSet<Disease> diseases) {
//        super(nom, sexe, poids, taille, age, moral, diseases);
//    }

    @Override
    public boolean die(Room room) {
        super.die(room);
        contaminate(this, room);
        return true;
    }
}