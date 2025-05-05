package com.fantasyhospital;

import com.fantasyhospital.model.creatures.Races;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.races.*;
import com.fantasyhospital.model.maladie.Maladie;

import java.util.HashSet;
import java.util.Random;

public class Game {
    static Random randomCreature = new Random();
    public static Creature randomCreature() {
        Races race = Races.values()[randomCreature.nextInt(Races.values().length)];

        HashSet<Maladie> maladie = new HashSet<>();
        maladie.add(new Maladie());

        Creature creature = switch (race) {
            case ELFE -> new Elfe();
            case ORQUE-> new Orque();
            case LYCANTHROPE -> new Lycanthrope();
            case NAIN -> new Nain();
            case REPTILIEN -> new Reptilien();
            case VAMPIRE -> new Vampire();
            case ZOMBIE -> new Zombie();
            case HOMME_BETE -> new HommeBete();
        };
        creature.setMaladies(maladie);
        return creature;
    }
}
