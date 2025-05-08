package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.HabitantTriage;
import com.fantasyhospital.model.creatures.interfaces.Contaminant;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;

import java.util.HashSet;

public class Lycanthrope extends HabitantTriage implements Contaminant {

    public Lycanthrope() {
        this( null);
    }

    public Lycanthrope(CopyOnWriteArrayList<Maladie> maladies) {
        super(maladies);
    }

    @Override
    public boolean trepasser(Salle salle) {
        super.trepasser(salle);
        contaminer(this, salle);
        return true;
    }
}