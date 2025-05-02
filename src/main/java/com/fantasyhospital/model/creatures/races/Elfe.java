package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.ClientVIP;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.interfaces.Demoralisant;
import com.fantasyhospital.model.maladie.Maladie;

import java.util.HashSet;
import java.util.List;


public class Elfe extends ClientVIP implements Demoralisant {

    public Elfe() {
        super(genererNomAleatoire(), genererSexeAleatoire(), genererPoids(), genererTaille(), genererAge(), genererMoral(), null);
    }

    public Elfe(HashSet<Maladie> maladies) {
        super(genererNomAleatoire(), genererSexeAleatoire(), genererPoids(), genererTaille(), genererAge(), genererMoral(), maladies);
    }

    public Elfe(String nomComplet, String sexe, int poids, int taille, int age, int moral, HashSet<Maladie> maladies) {
        super(nomComplet, sexe, poids, taille, age, moral, maladies);
    }

    public void trepasser(List<Creature> creatures) {
        demoraliser(creatures);
    }
}
