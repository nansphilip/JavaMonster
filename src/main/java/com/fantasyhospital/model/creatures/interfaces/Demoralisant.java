package com.fantasyhospital.model.creatures.interfaces;

import java.util.List;

import com.fantasyhospital.model.creatures.abstractclass.Creature;

public interface Demoralisant {

    default void demoraliser(List<Creature> creatures){
        //Body de la mtéthode contaminer commune à toutes les classes de l'interface
    }

}
