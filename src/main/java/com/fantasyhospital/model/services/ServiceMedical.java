package com.fantasyhospital.model.services;

import java.util.ArrayList;
import java.util.List;

import com.fantasyhospital.model.Creature;

public abstract class ServiceMedical {
    protected String nom;
    protected double superficie;
    protected int capaciteMax;
    protected List<Creature> creatures = new ArrayList<>();
    protected String budget; // inexistant, médiocre, insuffisant, faible

    public ServiceMedical(String nom, double superficie, int capaciteMax, String budget) {
        this.nom = nom;
        this.superficie = superficie;
        this.capaciteMax = capaciteMax;
        this.budget = budget;
    }

    public abstract void afficher();
    public abstract boolean ajouterCreature(Creature creature);
    public abstract boolean enleverCreature(Creature creature);
    public abstract void soignerCreatures();
    public abstract void reviserBudget();

    // Getters et setters omis pour la clarté
} 