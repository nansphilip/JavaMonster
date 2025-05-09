package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.ClientVIP;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.interfaces.Contaminant;
import com.fantasyhospital.model.creatures.interfaces.Demoralisant;
import com.fantasyhospital.model.creatures.interfaces.Regenerant;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class Vampire extends ClientVIP implements Regenerant, Contaminant, Demoralisant {

    public Vampire() {
        super(null);
    }

    public Vampire(CopyOnWriteArrayList<Maladie> maladies) {
        super(maladies);
    }

    //    public Vampire(String nomComplet, String sexe, int poids, int taille, int age, int moral, HashSet<Maladie> maladies) {
    //        super(nomComplet, sexe, poids, taille, age, moral, maladies);
    //    }

    @Override
    public boolean trepasser(Salle salle) {
        super.trepasser(salle);
        contaminer(this, salle);
        demoraliser(this, salle);
        return regenerer(this);
    }
}