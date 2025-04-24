package com.fantasyhospital.model;

public abstract class Maladie {
    protected String nomComplet;
    protected String nomAbrege;
    protected int niveauActuel;
    protected int niveauMax;

    public Maladie(String nomComplet, String nomAbrege, int niveauMax) {
        this.nomComplet = nomComplet;
        this.nomAbrege = nomAbrege;
        this.niveauMax = niveauMax;
        this.niveauActuel = 0;
    }

    public abstract void augmenterNiveau();
    public abstract void diminuerNiveau();
    public abstract void changerNiveau(int nouveauNiveau);
    public boolean estLethale() {
        return niveauActuel >= niveauMax;
    }
    // Getters et setters omis pour la clarté
} 