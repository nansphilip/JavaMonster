package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.ClientVIP;
import com.fantasyhospital.model.maladie.Maladie;

import java.util.HashSet;
import java.util.List;

public class Reptilien extends ClientVIP {

    public Reptilien() {
        super(genererNomAleatoire(), genererSexeAleatoire(), genererPoids(), genererTaille(), genererAge(), genererMoral(), null);
    }

    public Reptilien(HashSet<Maladie> maladies) {
        super(genererNomAleatoire(), genererSexeAleatoire(), genererPoids(), genererTaille(), genererAge(), genererMoral(), maladies);
    }

    public Reptilien(String nomComplet, String sexe, int poids, int taille, int age, int moral, HashSet<Maladie> maladies) {
        super(nomComplet, sexe, poids, taille, age, moral, maladies);
    }
}