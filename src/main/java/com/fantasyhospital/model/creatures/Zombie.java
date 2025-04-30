package com.fantasyhospital.model.creatures;

import com.fantasyhospital.model.Creature;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.model.interfaces.Regenerant;

public class Zombie extends Creature implements Regenerant {
    public Zombie(String nom, String sexe, double poids, double taille, int age) {
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
    public void regenerer() { /* ... */ }

} 