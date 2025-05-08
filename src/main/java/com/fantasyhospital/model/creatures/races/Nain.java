package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.ClientVIP;
import com.fantasyhospital.model.maladie.Maladie;

import java.util.concurrent.CopyOnWriteArrayList;

public class Nain extends ClientVIP {

    public Nain() {
        super(null);
    }

    public Nain(CopyOnWriteArrayList<Maladie> maladies) {
        super(maladies);
    }

//    public Nain(String nomComplet, String sexe, int poids, int taille, int age, int moral, HashSet<Maladie> maladies) {
//        super(nomComplet, sexe, poids, taille, age, moral, maladies);
//    }
}