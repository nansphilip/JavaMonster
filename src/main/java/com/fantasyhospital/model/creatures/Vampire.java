package com.fantasyhospital.model.creatures;

import com.fantasyhospital.model.Creature;
import com.fantasyhospital.model.Maladie;
import com.fantasyhospital.model.interfaces.Bestial;
import com.fantasyhospital.model.interfaces.Contagieux;
import com.fantasyhospital.model.interfaces.Regenerant;
import com.fantasyhospital.model.interfaces.VIP;

public class Vampire extends Creature implements Bestial, Regenerant, Contagieux, VIP {
    public Vampire(String nom, String sexe, double poids, double taille, int age) {
        super(nom, sexe, poids, taille, age);
    }

    @Override
    public void attendre() { /* ... */ }
    @Override
    public void hurler() { /* ... */ }
    @Override
    public void semporter() { /* ... */ }
    @Override
    public void tomberMalade(Maladie maladie) { /* ... */ }
    @Override
    public void soigner() { /* ... */ }
    @Override
    public void trepasser() { /* ... */ }

    @Override
    public void contaminer(Creature cible) { /* ... */ }
    @Override
    public void regenerer() { /* ... */ }
    @Override
    public boolean estContagieux() { return true; }
    @Override
    public void traitementVIP() { /* ... */ }
} 