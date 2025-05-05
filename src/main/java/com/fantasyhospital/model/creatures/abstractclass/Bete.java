package com.fantasyhospital.model.creatures.abstractclass;

import com.fantasyhospital.Simulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Random;

public abstract class Bete {

    protected String nomComplet;
    protected String sexe;
    protected int poids;
    protected int taille;
    protected int age;
    protected int moral;
    public static final String[] arrayNomMasculin = {"Lucien", "Jacques", "Marcel", "Fernand", "Albert", "Léon", "Raymond", "Gaston", "Henri", "Maurice", "Roger", "René", "André", "Georges", "Paul", "Émile", "Ernest", "Eugène", "Armand", "Anatole", "Gustave", "Alphonse", "Achille", "Aimé", "Félix", "Théophile", "Léopold", "Désiré", "Jules", "Joseph", "Clément", "Baptiste", "Philibert", "Basile", "Constant", "Léonard", "Prosper", "Anatole", "Honoré", "Octave"};
    public static final ArrayList<String> nomsMasculins = new ArrayList<>(Arrays.asList(arrayNomMasculin));
    public static final String[] arrayNomFeminin = {"Germaine", "Georgette", "Yvonne", "Paulette", "Raymonde", "Lucienne", "Andrée", "Marcelle", "Henriette", "Simone", "Denise", "Suzanne", "Renée", "Fernande", "Berthe", "Jeanne", "Marguerite", "Augustine", "Albertine", "Léontine", "Émilienne", "Antoinette", "Clémentine", "Eugénie", "Philomène", "Odette", "Colette", "Huguette", "Pierrette", "Thérèse", "Joséphine", "Amélie", "Irène", "Bertille", "Hortense", "Édith", "Noëlie", "Armande", "Honorine", "Cunégonde"};
    public static final ArrayList<String> nomsFeminins = new ArrayList<>(Arrays.asList(arrayNomFeminin));
    public static Random random = new Random();
    private static final Logger logger = LoggerFactory.getLogger(Bete.class);

    public Bete(String nomComplet, String sexe, int poids, int taille, int age, int moral) {
        this.nomComplet = nomComplet;
        this.sexe = sexe;
        this.poids = poids;
        this.taille = taille;
        this.age = age;
        this.moral = moral;
    }

    public static String genererNomAleatoire() {
//        String[] prefix = {"Kra", "Zor", "El", "Thra", "Gor", "Vel", "Mor", "Sha", "Lun", "Dra"};
//        String[] suffix = {"gor", "nax", "iel", "dor", "vak", "mir", "thar", "dil", "rak", "zul"};
//
//        return prefix[random.nextInt(prefix.length)] + suffix[random.nextInt(suffix.length)];
        String nom ="";
        String sexe = "M";
        switch (sexe) {
            case "M":
                int i = random.nextInt(Bete.nomsMasculins.size());
                nom = Bete.nomsMasculins.get(i);
                Bete.nomsMasculins.remove(i);
            case "F":
                i = random.nextInt(Bete.nomsFeminins.size());
                nom = Bete.nomsFeminins.get(i);
                Bete.nomsFeminins.remove(i);
        }
        return nom;
    }

    public abstract void attendre();

    public void trepasser(LinkedHashSet<Creature> creatures) {
        //mourir
        logger.info("La créature {} se meurt.", this.nomComplet);
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public int getPoids() {
        return poids;
    }

    public void setPoids(int poids) {
        this.poids = poids;
    }

    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getMoral() {
        return moral;
    }

    public void setMoral(int moral) {
        this.moral = moral;
    }
}
