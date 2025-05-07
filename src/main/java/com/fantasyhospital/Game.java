package com.fantasyhospital;

import java.util.HashSet;
import java.util.Random;

import com.fantasyhospital.model.creatures.Races;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.races.Elfe;
import com.fantasyhospital.model.creatures.races.HommeBete;
import com.fantasyhospital.model.creatures.races.Lycanthrope;
import com.fantasyhospital.model.creatures.races.Nain;
import com.fantasyhospital.model.creatures.races.Orque;
import com.fantasyhospital.model.creatures.races.Reptilien;
import com.fantasyhospital.model.creatures.races.Vampire;
import com.fantasyhospital.model.creatures.races.Zombie;
import com.fantasyhospital.model.maladie.Maladie;

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
