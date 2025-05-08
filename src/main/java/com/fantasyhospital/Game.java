package com.fantasyhospital;

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fantasyhospital.enums.RaceType;
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

/**
 * Classe utilitaire pour la génération aléatoire de créatures dans la
 * simulation Fantasy Hospital.
 * <p>
 * Fournit une méthode statique permettant de créer une créature d'une race
 * aléatoire, avec au moins une maladie initiale.
 * </p>
 */
public class Game {

    /**
     * Générateur de nombres aléatoires utilisé pour la sélection des races.
     */
    static Random randomCreature = new Random();

    /**
     * Génère une créature aléatoire avec une maladie.
     *
     * @return une instance de {@link Creature} d'une race aléatoire, avec une
     * maladie initiale.
     */
    public static Creature randomCreature() {
        RaceType race = RaceType.values()[randomCreature.nextInt(RaceType.values().length)];

        CopyOnWriteArrayList<Maladie> maladie = new CopyOnWriteArrayList<>();
        maladie.add(new Maladie());

        Creature creature = switch (race) {
            case ELFE ->
                new Elfe();
            case ORQUE ->
                new Orque();
            case LYCANTHROPE ->
                new Lycanthrope();
            case NAIN ->
                new Nain();
            case REPTILIEN ->
                new Reptilien();
            case VAMPIRE ->
                new Vampire();
            case ZOMBIE ->
                new Zombie();
            case HOMME_BETE ->
                new HommeBete();
        };
        creature.setMaladies(maladie);
        return creature;
    }
}
