package com.fantasyhospital.model.maladie;

public class Maladie {
    protected String nomComplet;
    protected String nomAbrege;
    protected static int NIVEAU_MAX;
    protected int niveauActuel;

    public Maladie(String nomComplet, String nomAbrege, int NIVEAU_MAX, int niveauActuel) {
        this.nomComplet = nomComplet;
        this.nomAbrege = nomAbrege;
        Maladie.NIVEAU_MAX = NIVEAU_MAX;
        this.niveauActuel = niveauActuel;
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
} 