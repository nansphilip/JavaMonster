package com.fantasyhospital.model.services;

import com.fantasyhospital.model.Creature;
import com.fantasyhospital.model.Service;

public class Crypte extends Service {
    private int ventilation;
    private double temperature;

    public Crypte(String nom, double superficie, int capaciteMax, String budget, int ventilation, double temperature) {
        super(nom, superficie, capaciteMax, budget);
        this.ventilation = ventilation;
        this.temperature = temperature;
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