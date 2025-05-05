package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.HabitantTriage;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.interfaces.Contaminant;
import com.fantasyhospital.model.maladie.Maladie;

import java.util.HashSet;
import java.util.List;

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

    public void trepasser(Creature creature){
        contaminer(creature);
    }
}