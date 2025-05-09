package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.HabitantTriage;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.model.creatures.interfaces.Regenerant;
import com.fantasyhospital.salles.Salle;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class Zombie extends HabitantTriage implements Regenerant {

    public Zombie() {
        super(null);
    }

    public Zombie(CopyOnWriteArrayList<Maladie> maladies) {
        super(maladies);
    }

//    public Zombie(String nom, String sexe, int poids, int taille, int age, int moral, HashSet<Maladie> maladies) {
//        super(nom, sexe, poids, taille, age, moral, maladies);
//    }

    @Override
    public boolean trepasser(Salle salle) {
        super.trepasser(salle);
        return regenerer(this);
    }
}