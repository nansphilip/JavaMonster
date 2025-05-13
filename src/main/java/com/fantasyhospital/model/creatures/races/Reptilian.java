package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.VIPPatient;
import com.fantasyhospital.model.disease.Disease;

import java.util.concurrent.CopyOnWriteArrayList;

public class Reptilian extends VIPPatient {

    public Reptilian() {
        super(null);
    }

    public Reptilian(CopyOnWriteArrayList<Disease> diseases) {
        super(diseases);
    }

//    public Reptilien(String nomComplet, String sexe, int poids, int taille, int age, int moral, HashSet<Disease> diseases) {
//        super(nomComplet, sexe, poids, taille, age, moral, diseases);
//    }
}