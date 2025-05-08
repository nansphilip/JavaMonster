package com.fantasyhospital.model.creatures.races;

import java.util.HashSet;

import com.fantasyhospital.model.creatures.ClientVIP;
import com.fantasyhospital.model.maladie.Maladie;

public class Nain extends ClientVIP {

    public Nain() {
        super(null);
    }

    public Nain(HashSet<Maladie> maladies) {
        super(maladies);
    }

    //    public Nain(String nomComplet, String sexe, int poids, int taille, int age, int moral, HashSet<Maladie> maladies) {
    //        super(nomComplet, sexe, poids, taille, age, moral, maladies);
    //    }
}
