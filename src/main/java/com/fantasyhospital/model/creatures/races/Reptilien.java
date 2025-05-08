package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.ClientVIP;
import com.fantasyhospital.model.maladie.Maladie;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;

public class Reptilien extends ClientVIP {

    public Reptilien() {
        super(null);
    }

    public Reptilien(CopyOnWriteArrayList<Maladie> maladies) {
        super(maladies);
    }

//    public Reptilien(String nomComplet, String sexe, int poids, int taille, int age, int moral, HashSet<Maladie> maladies) {
//        super(nomComplet, sexe, poids, taille, age, moral, maladies);
//    }
}