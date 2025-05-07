package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.ClientVIP;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.interfaces.Contaminant;
import com.fantasyhospital.model.creatures.interfaces.Demoralisant;
import com.fantasyhospital.model.creatures.interfaces.Regenerant;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;

import java.util.HashSet;
import java.util.List;

public class Vampire extends ClientVIP implements Regenerant, Contaminant, Demoralisant {

    public Vampire() {
        super(null);
    }

    public Vampire(HashSet<Maladie> maladies) {
        super(maladies);
    }

    //    public Vampire(String nomComplet, String sexe, int poids, int taille, int age, int moral, HashSet<Maladie> maladies) {
    //        super(nomComplet, sexe, poids, taille, age, moral, maladies);
    //    }


    @Override
    public void trepasser(Salle salle) {
        super.trepasser(salle);
        demoraliser(this, salle);
        contaminer(this, salle);
    }
}