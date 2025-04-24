package com.fantasyhospital.model;

public class MDC extends Maladie {
    public MDC(int niveauMax) {
        super("Maladie Débilitante Chronique", "MDC", niveauMax);
    }

    @Override
    public void augmenterNiveau() { niveauActuel++; }
    @Override
    public void diminuerNiveau() { if (niveauActuel > 0) niveauActuel--; }
    @Override
    public void changerNiveau(int nouveauNiveau) { niveauActuel = nouveauNiveau; }
} 