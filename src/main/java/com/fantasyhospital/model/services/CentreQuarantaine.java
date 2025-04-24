package com.fantasyhospital.model.services;

import com.fantasyhospital.model.Creature;

public class CentreQuarantaine extends ServiceMedical {
    private boolean isolation;

    public CentreQuarantaine(String nom, double superficie, int capaciteMax, String budget, boolean isolation) {
        super(nom, superficie, capaciteMax, budget);
        this.isolation = isolation;
    }

    @Override
    public void afficher() { /* ... */ }
    @Override
    public boolean ajouterCreature(Creature creature) { /* ... */ return false; }
    @Override
    public boolean enleverCreature(Creature creature) { /* ... */ return false; }
    @Override
    public void soignerCreatures() { /* ... */ }
    @Override
    public void reviserBudget() { /* ... */ }

    // Getters et setters omis pour la clarté
} 