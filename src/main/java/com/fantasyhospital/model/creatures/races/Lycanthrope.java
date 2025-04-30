package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.HabitantTriage;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.interfaces.Contaminant;
import com.fantasyhospital.model.maladie.Maladie;

import java.util.List;

public class Lycanthrope extends HabitantTriage implements Contaminant {

    public Lycanthrope(String nom, String sexe, int poids, int taille, int age, int moral, List<Maladie> maladies) {
        super(nom, sexe, poids, taille, age, moral, maladies);
    }

    public void trepasser(Creature creature){
        contaminer(creature);
    }
}