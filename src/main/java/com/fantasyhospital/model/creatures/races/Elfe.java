package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.ClientVIP;
import com.fantasyhospital.model.creatures.interfaces.Demoralisant;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;
import java.util.concurrent.CopyOnWriteArrayList;


public class Elfe extends ClientVIP implements Demoralisant {

    public Elfe() {
        super(null);
    }

    public Elfe(CopyOnWriteArrayList<Maladie> maladies) {
        super(maladies);
    }

//    public Elfe(String nomComplet, String sexe, int poids, int taille, int age, int moral, HashSet<Maladie> maladies) {
//        super(nomComplet, sexe, poids, taille, age, moral, maladies);
//    }

    @Override
    public boolean trepasser(Salle salle) {
        super.trepasser(salle);
        demoraliser(this, salle);
        return true;
    }
}
