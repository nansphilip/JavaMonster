package com.fantasyhospital.model;

public class NDMAD extends Maladie {
    public NDMAD(int niveauMax) {
        super("Nom de maladie à définir", "NDMAD", niveauMax);
    }

    @Override
    public void augmenterNiveau() { niveauActuel++; }
    @Override
    public void diminuerNiveau() { if (niveauActuel > 0) niveauActuel--; }
    @Override
    public void changerNiveau(int nouveauNiveau) { niveauActuel = nouveauNiveau; }
} 