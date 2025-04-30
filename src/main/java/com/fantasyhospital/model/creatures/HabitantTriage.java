package com.fantasyhospital.model.creatures;

import com.fantasyhospital.model.maladie.Maladie;

import java.util.List;

public abstract class HabitantTriage extends Creature {

    public HabitantTriage(String nom, String sexe, int poids, int taille, int age, int moral,  List<Maladie> maladies) {
        super(nom, sexe, poids, taille, age, moral, maladies);
    }

    public void attendre(){
        //attendre patiemment si il est avec au moins 1 autre creature meme espece
    }
}
