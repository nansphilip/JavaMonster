package com.fantasyhospital.model.creatures;

import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.maladie.Maladie;

import java.util.List;

public abstract class ClientVIP extends Creature {

    public ClientVIP(String nomComplet, String sexe, int poids, int taille, int age, int moral, List<Maladie> maladies) {
        super(nomComplet, sexe, poids, taille, age, moral, maladies);
    }

    public void attendre(){
        //moral tombe au plus bas si attend trop longtemps
    }
}
