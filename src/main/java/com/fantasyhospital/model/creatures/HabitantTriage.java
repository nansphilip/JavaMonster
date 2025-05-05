package com.fantasyhospital.model.creatures;

import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.maladie.Maladie;

import java.util.HashSet;

public abstract class HabitantTriage extends Creature {

    public HabitantTriage(HashSet<Maladie> maladies) {
        super(maladies);
    }

    public void attendre(){
        //attendre patiemment si il est avec au moins 1 autre creature meme espece
    }
}
