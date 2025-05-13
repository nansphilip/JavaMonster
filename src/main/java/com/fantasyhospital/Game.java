package com.fantasyhospital;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fantasyhospital.enums.RaceType;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.races.Elf;
import com.fantasyhospital.model.creatures.races.Werebeast;
import com.fantasyhospital.model.creatures.races.Lycanthrope;
import com.fantasyhospital.model.creatures.races.Dwarf;
import com.fantasyhospital.model.creatures.races.Orc;
import com.fantasyhospital.model.creatures.races.Reptilian;
import com.fantasyhospital.model.creatures.races.Vampire;
import com.fantasyhospital.model.creatures.races.Zombie;
import com.fantasyhospital.model.disease.Disease;

/**
 * Utility class for randomly generating creatures in the Fantasy Hospital simulation.
 * <p>
 * Provides a static method to create a creature of a random race, with at least one initial disease.
 * </p>
 */
public class Game {

    /**
     * Random number generator used for selecting races.
     */
    static Random randomCreature = new Random();

    /**
     * Generates a random creature with a disease.
     *
     * @return an instance of {@link Creature} with a random race and an initial disease.
     */
    public static Creature randomCreature() {
        RaceType race = RaceType.values()[randomCreature.nextInt(RaceType.values().length)];

        CopyOnWriteArrayList<Disease> disease = new CopyOnWriteArrayList<>();
        disease.add(new Disease());

        Creature creature = switch (race) {
            case ELFE ->
                new Elf();
            case ORQUE ->
                new Orc();
            case LYCANTHROPE ->
                new Lycanthrope();
            case NAIN ->
                new Dwarf();
            case REPTILIEN ->
                new Reptilian();
            case VAMPIRE ->
                new Vampire();
            case ZOMBIE ->
                new Zombie();
            case HOMME_BETE ->
                new Werebeast();
        };
        creature.setDiseases(disease);
        return creature;
    }
}
