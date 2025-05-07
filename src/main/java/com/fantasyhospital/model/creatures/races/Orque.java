package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.HabitantTriage;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.interfaces.Contaminant;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Orque extends HabitantTriage implements Contaminant {

    public Orque() {
        super(null);
    }

    public Orque(HashSet<Maladie> maladies) {
        super(maladies);
    }

//    public Orque(String nom, String sexe, int poids, int taille, int age, int moral, HashSet<Maladie> maladies) {
//        super(nom, sexe, poids, taille, age, moral, maladies);
//    }

    @Override
    public void trepasser(Salle salle) {
        super.trepasser(salle);
        contaminer(this, salle);
    }
}