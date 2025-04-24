package com.fantasyhospital.model;

public class FOMO extends Maladie {
    public FOMO(int niveauMax) {
        super("Syndrome fear of missing out", "FOMO", niveauMax);
    }

    @Override
    public void augmenterNiveau() { niveauActuel++; }
    @Override
    public void diminuerNiveau() { if (niveauActuel > 0) niveauActuel--; }
    @Override
    public void changerNiveau(int nouveauNiveau) { niveauActuel = nouveauNiveau; }
} 