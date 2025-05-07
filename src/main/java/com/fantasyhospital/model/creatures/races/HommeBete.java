package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.HabitantTriage;
import com.fantasyhospital.model.creatures.interfaces.Contaminant;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;

import java.util.HashSet;

public class HommeBete extends HabitantTriage implements Contaminant {

    public HommeBete() {
        super(null);
    }

    public HommeBete(HashSet<Maladie> maladies) {
        super(maladies);
    }
//
//    public HommeBete(String nom, String sexe, int poids, int taille, int age, int moral, HashSet<Maladie> maladies) {
//        super(nom, sexe, poids, taille, age, moral, maladies);
//    }

    @Override
    public void trepasser(Salle salle) {
        super.trepasser(salle);
        contaminer(this, salle);
    }
}