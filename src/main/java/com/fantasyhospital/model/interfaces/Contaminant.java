package com.fantasyhospital.model.interfaces;

import com.fantasyhospital.model.creatures.Creature;

public interface Contaminant {

    default void contaminer(Creature cible){
        //Body de la mtéthode contaminer commune à toutes les classes de l'interface
    }
} 
