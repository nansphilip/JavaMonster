package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.HabitantTriage;
import com.fantasyhospital.model.creatures.interfaces.Contaminant;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;

import java.util.concurrent.CopyOnWriteArrayList;

public class HommeBete extends HabitantTriage implements Contaminant {

    public HommeBete() {
        super(null);
    }

    public HommeBete(CopyOnWriteArrayList<Maladie> maladies) {
        super(maladies);
    }
//
//    public HommeBete(String nom, String sexe, int poids, int taille, int age, int moral, HashSet<Maladie> maladies) {
//        super(nom, sexe, poids, taille, age, moral, maladies);
//    }

    @Override
    public boolean trepasser(Salle salle) {
        super.trepasser(salle);
        contaminer(this, salle);
        return true;
    }
}