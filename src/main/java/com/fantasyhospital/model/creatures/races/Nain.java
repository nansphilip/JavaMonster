package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.ClientVIP;
import com.fantasyhospital.model.maladie.Maladie;

import java.util.List;

public class Nain extends ClientVIP {

    public Nain() {
        super(genererNomAleatoire(), genererSexeAleatoire(), genererPoids(), genererTaille(), genererAge(), genererMoral(), null);
    }

    public Nain(List<Maladie> maladies) {
        super(genererNomAleatoire(), genererSexeAleatoire(), genererPoids(), genererTaille(), genererAge(), genererMoral(), maladies);
    }

    public Nain(String nomComplet, String sexe, int poids, int taille, int age, int moral, List<Maladie> maladies) {
        super(nomComplet, sexe, poids, taille, age, moral, maladies);
    }
}