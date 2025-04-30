package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.ClientVIP;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.interfaces.Contaminant;
import com.fantasyhospital.model.creatures.interfaces.Demoralisant;
import com.fantasyhospital.model.creatures.interfaces.Regenerant;
import com.fantasyhospital.model.maladie.Maladie;

import java.util.List;

public class Vampire extends ClientVIP implements Regenerant, Contaminant, Demoralisant {

    public Vampire() {
        super(genererNomAleatoire(), genererSexeAleatoire(), genererPoids(), genererTaille(), genererAge(), genererMoral(), null);
    }

    public Vampire(List<Maladie> maladies) {
        super(genererNomAleatoire(), genererSexeAleatoire(), genererPoids(), genererTaille(), genererAge(), genererMoral(), maladies);
    }

    public Vampire(String nomComplet, String sexe, int poids, int taille, int age, int moral, List<Maladie> maladies) {
        super(nomComplet, sexe, poids, taille, age, moral, maladies);
    }

    public void trepasser(List<Creature> creatures) {
        regenerer();
        //contaminer(creatures); //prendre une creature au hasard dans la liste de creatures
        demoraliser(creatures);
    }
}