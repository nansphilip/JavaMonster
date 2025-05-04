package com.fantasyhospital.model.maladie;

import com.fantasyhospital.MaladieType;

import java.util.*;

public class Maladie {
    protected MaladieType type;
    protected final int NIVEAU_MAX;
    protected int niveauActuel;

    public static Random random = new Random();

    public Maladie(){
        this.type = getRandomType();
        this.NIVEAU_MAX = genererNiveauMaxAleatoire();
        this.niveauActuel = 1;
    }

    public Maladie(MaladieType type, int NIVEAU_MAX, int niveauActuel) {
        this.type = type;
        this.NIVEAU_MAX = NIVEAU_MAX;
        this.niveauActuel = niveauActuel;
    }

    public static MaladieType getRandomType() {
        MaladieType[] values = MaladieType.values();
        return values[random.nextInt(values.length)];
    }

    public int getImpactMoral() {
        return type.getImpactMoral();
    }

    public String getNom() {
        return type.name();
    }

    public String getNomComplet() {
        return type.getNomComplet();
    }

    public int genererNiveauMaxAleatoire() {
        return 5 + random.nextInt(5);
    }

    public void augmenterNiveau(){
        if(this.niveauActuel < NIVEAU_MAX){
            this.niveauActuel++;
        }
    }

    public void diminuerNiveau(){
        if(this.niveauActuel > 0){
            this.niveauActuel--;
        }
    }

    public void changerNiveau(int nouveauNiveau){
        if(nouveauNiveau < NIVEAU_MAX &&  nouveauNiveau > 0){
            this.niveauActuel = nouveauNiveau;
        }
    }

    public boolean estLethale() {
        return niveauActuel == NIVEAU_MAX;
    }

    // Getters et setters omis pour la clarté
    public int getNIVEAU_MAX() {
        return NIVEAU_MAX;
    }

//    public void setNIVEAU_MAX(int NIVEAU_MAX) {
//        this.NIVEAU_MAX = NIVEAU_MAX;
//    }

    public int getNiveauActuel() {
        return niveauActuel;
    }

    public void setNiveauActuel(int niveauActuel) {
        this.niveauActuel = niveauActuel;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return " " + type +
                " Nom Complet='" + getNomComplet() + '\'' +
                ", NIVEAU_MAX=" + NIVEAU_MAX +
                ", niveauActuel=" + niveauActuel;
    }
} 