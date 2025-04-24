package com.fantasyhospital.model.maladies;

import com.fantasyhospital.model.Maladie;

public class ZPL extends Maladie {
    public ZPL(int niveauMax) {
        super("Zoopathie paraphrénique lycanthropique", "ZPL", niveauMax);
    }

    @Override
    public void augmenterNiveau() { niveauActuel++; }
    @Override
    public void diminuerNiveau() { if (niveauActuel > 0) niveauActuel--; }
    @Override
    public void changerNiveau(int nouveauNiveau) { niveauActuel = nouveauNiveau; }
} 