package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.VIPPatient;
import com.fantasyhospital.model.creatures.interfaces.Contaminant;
import com.fantasyhospital.model.creatures.interfaces.Demoralizing;
import com.fantasyhospital.model.creatures.interfaces.Regenerating;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.rooms.Room;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class Vampire extends VIPPatient implements Regenerating, Contaminant, Demoralizing {

    public Vampire() {
        super(null);
    }

    public Vampire(CopyOnWriteArrayList<Disease> diseases) {
        super(diseases);
    }

    //    public Vampire(String nomComplet, String sexe, int poids, int taille, int age, int moral, HashSet<Disease> diseases) {
    //        super(nomComplet, sexe, poids, taille, age, moral, diseases);
    //    }

    @Override
    public boolean die(Room room) {
        super.die(room);
        contaminate(this, room);
        demoralize(this, room);
        return regenerate(this);
    }
}