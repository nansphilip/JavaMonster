package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.ClientVIP;
import com.fantasyhospital.model.maladie.Maladie;

import java.util.List;

public class Reptilien extends ClientVIP {

    public Reptilien(String nomComplet, String sexe, int poids, int taille, int age, int moral, List<Maladie> maladies) {
        super(nomComplet, sexe, poids, taille, age, moral, maladies);
    }
}