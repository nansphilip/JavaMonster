package com.fantasyhospital.model.creatures;

import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.maladie.Maladie;

import java.util.HashSet;

public abstract class ClientVIP extends Creature {

    public ClientVIP( HashSet<Maladie> maladies) {
        super( maladies);
    }

    public void attendre(){
        //moral tombe au plus bas si attend trop longtemps
    }
}
