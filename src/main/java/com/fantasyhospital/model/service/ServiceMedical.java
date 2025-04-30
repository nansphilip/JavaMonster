package com.fantasyhospital.model.service;

import com.fantasyhospital.model.Creature;
import com.fantasyhospital.model.creatures.Medecin;

import java.util.ArrayList;
import java.util.List;

public class ServiceMedical {
    protected String nom;
    protected double superficie;
    protected final int NB_MAX_CREATURE;
    protected List<Creature> creatures = new ArrayList<>();
    protected String budget; // inexistant, médiocre, insuffisant, faible

    public ServiceMedical(String nom, double superficie, int NB_MAX_CREATURE, String budget) {
        this.nom = nom;
        this.superficie = superficie;
        this.NB_MAX_CREATURE = NB_MAX_CREATURE;
        this.budget = budget;
    }

    public void afficherInfosService(){

    }

    public void afficherInfosCreatures(){

    }

    public boolean ajouterCreature(Creature creature){
        return false;
    }

    public boolean enleverCreature(Creature creature){
        return false;
    }

    public void soignerCreatures(Medecin medecin, Creature creature){

    }

    public void reviserBudget(int valeur){

    }

    // Getters et setters omis pour la clarté
} 