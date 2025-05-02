package com.fantasyhospital.model.creatures;

import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.maladie.Maladie;

import java.util.HashSet;

public abstract class HabitantTriage extends Creature {

    public HabitantTriage(String nom, String sexe, int poids, int taille, int age, int moral,  HashSet<Maladie> maladies) {
        super(nom, sexe, poids, taille, age, moral, maladies);
    }

    public void attendre(){
        //attendre patiemment si il est avec au moins 1 autre creature meme espece
    }
}
