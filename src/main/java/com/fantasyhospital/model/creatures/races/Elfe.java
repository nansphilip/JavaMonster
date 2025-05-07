package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.ClientVIP;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.interfaces.Demoralisant;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;

public class Elfe extends ClientVIP implements Demoralisant {

    public Elfe() {
        super(null);
    }

    public Elfe(HashSet<Maladie> maladies) {
        super(maladies);
    }

//    public Elfe(String nomComplet, String sexe, int poids, int taille, int age, int moral, HashSet<Maladie> maladies) {
//        super(nomComplet, sexe, poids, taille, age, moral, maladies);
//    }

    @Override
    public void trepasser(Salle salle) {
        super.trepasser(salle);
        demoraliser(this, salle);
    }
}
