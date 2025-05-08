package com.fantasyhospital.model.creatures.races;

import java.util.HashSet;

import com.fantasyhospital.model.creatures.ClientVIP;
import com.fantasyhospital.model.maladie.Maladie;

public class Reptilien extends ClientVIP {

    public Reptilien() {
        super(null);
    }

    public Reptilien(HashSet<Maladie> maladies) {
        super(maladies);
    }

    //    public Reptilien(String nomComplet, String sexe, int poids, int taille, int age, int moral, HashSet<Maladie> maladies) {
    //        super(nomComplet, sexe, poids, taille, age, moral, maladies);
    //    }
}
