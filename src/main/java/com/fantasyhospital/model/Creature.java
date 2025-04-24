package com.fantasyhospital.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Creature {
    protected String nom;
    protected String sexe;
    protected double poids;
    protected double taille;
    protected int age;
    protected int moral;
    protected List<Maladie> maladies = new ArrayList<>();

    public Creature(String nom, String sexe, double poids, double taille, int age) {
        this.nom = nom;
        this.sexe = sexe;
        this.poids = poids;
        this.taille = taille;
        this.age = age;
        this.moral = 100;
    }

    public abstract void attendre();
    public abstract void hurler();
    public abstract void semporter();
    public abstract void tomberMalade(Maladie maladie);
    public abstract void soigner();
    public abstract void trepasser();

    // Getters et setters omis pour la clarté
} 