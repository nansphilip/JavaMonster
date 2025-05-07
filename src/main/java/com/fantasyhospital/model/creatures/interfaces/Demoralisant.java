package com.fantasyhospital.model.creatures.interfaces;

import java.util.List;
import java.util.Random;

import com.fantasyhospital.model.creatures.abstractclass.Creature;

public interface Demoralisant {

    default void demoraliser(List<Creature> creatures){
        //Body de la mtéthode contaminer commune à toutes les classes de l'interface
        Random random = new Random();
        Creature c1 = creatures.get(random.nextInt(creatures.size()));
        Creature c2 = creatures.get(random.nextInt(creatures.size()));
        int moral = Math.max(c1.getMoral() - 10, 0);
        c1.setMoral(moral);
        moral = Math.max(c2.getMoral() - 10, 0);
        c2.setMoral(moral);
    }
}