package com.fantasyhospital;

import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.races.*;
import com.fantasyhospital.model.maladie.Maladie;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Game {

    public static Creature randomCreature() {
        Random randomCreature = new Random();

        String[] races = {"Elfe", "Orque", "Lycanthrope", "Nain", "Reptilien", "Vampire", "Zombie", "HommeBete"};
        String race = races[randomCreature.nextInt(races.length)];

        HashSet<Maladie> maladie = new HashSet<>();
        maladie.add(new Maladie());

        switch (race) {
            case "Elfe":
                return new Elfe(maladie);
            case "Orque":
                return new Orque(maladie);
            case "Lycanthrope":
                return new Lycanthrope(maladie);
            case "Nain":
                return new Nain(maladie);
            case "Reptilien":
                return new Reptilien(maladie);
            case "Vampire":
                return new Vampire(maladie);
            case "Zombie":
                return new Zombie(maladie);
            case "HommeBete":
                return new HommeBete(maladie);
            default:
                return null;
        }
    }
}
