package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.VIPPatient;
import com.fantasyhospital.model.disease.Disease;

import java.util.concurrent.CopyOnWriteArrayList;

public class Dwarf extends VIPPatient {

    public Dwarf() {
        super(null);
    }

    public Dwarf(CopyOnWriteArrayList<Disease> diseases) {
        super(diseases);
    }

//    public Nain(String nomComplet, String sexe, int poids, int taille, int age, int moral, HashSet<Disease> diseases) {
//        super(nomComplet, sexe, poids, taille, age, moral, diseases);
//    }
}