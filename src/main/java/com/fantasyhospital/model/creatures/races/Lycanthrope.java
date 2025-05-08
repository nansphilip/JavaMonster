package com.fantasyhospital.model.creatures.races;

import java.util.HashSet;

import com.fantasyhospital.model.creatures.HabitantTriage;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.interfaces.Contaminant;
import com.fantasyhospital.model.maladie.Maladie;

public class Lycanthrope extends HabitantTriage implements Contaminant {

    public Lycanthrope() {
        this(null);
    }

    public Lycanthrope(HashSet<Maladie> maladies) {
        super(maladies);
    }

    public void trepasser(Creature creature) {
        contaminer(creature);
    }
}
