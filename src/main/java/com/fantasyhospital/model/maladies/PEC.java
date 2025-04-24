package com.fantasyhospital.model.maladies;

import com.fantasyhospital.model.Maladie;

public class PEC extends Maladie {
    public PEC(int niveauMax) {
        super("Porphyrie érythropoïétique congénitale", "PEC", niveauMax);
    }

    @Override
    public void augmenterNiveau() { niveauActuel++; }
    @Override
    public void diminuerNiveau() { if (niveauActuel > 0) niveauActuel--; }
    @Override
    public void changerNiveau(int nouveauNiveau) { niveauActuel = nouveauNiveau; }
} 