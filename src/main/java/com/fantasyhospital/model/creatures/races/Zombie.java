package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.TriageResident;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.model.creatures.interfaces.Regenerating;
import com.fantasyhospital.rooms.Room;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class Zombie extends TriageResident implements Regenerating {

    public Zombie() {
        super(null);
    }

    public Zombie(CopyOnWriteArrayList<Disease> diseases) {
        super(diseases);
    }

//    public Zombie(String nom, String sexe, int poids, int taille, int age, int moral, HashSet<Disease> diseases) {
//        super(nom, sexe, poids, taille, age, moral, diseases);
//    }

    @Override
    public boolean die(Room room) {
        super.die(room);
        return regenerate(this);
    }
}