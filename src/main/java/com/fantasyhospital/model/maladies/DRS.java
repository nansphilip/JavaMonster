package com.fantasyhospital.model;

public class DRS extends Maladie {
    public DRS(int niveauMax) {
        super("Dépendance aux réseaux sociaux", "DRS", niveauMax);
    }

    @Override
    public void augmenterNiveau() { niveauActuel++; }
    @Override
    public void diminuerNiveau() { if (niveauActuel > 0) niveauActuel--; }
    @Override
    public void changerNiveau(int nouveauNiveau) { niveauActuel = nouveauNiveau; }
} 