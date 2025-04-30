package com.fantasyhospital.model.creatures.interfaces;

import com.fantasyhospital.model.creatures.abstractclass.Creature;

public interface Contaminant {

    default void contaminer(Creature cible){
        //Body de la mtéthode contaminer commune à toutes les classes de l'interface
    }
} 
