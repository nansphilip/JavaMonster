package com.fantasyhospital.model.servicemedical;

import com.fantasyhospital.model.creatures.Creature;
import com.fantasyhospital.model.creatures.Medecin;

import java.util.ArrayList;
import java.util.List;

public class ServiceMedical {
    protected String nom;
    protected double superficie;
    protected final int NB_MAX_CREATURE;
    protected List<Creature> creatures = new ArrayList<>();
    protected List<Medecin> medecins = new ArrayList<>();
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

    public void ajouterMedecin(Medecin medecin){
        this.medecins.add(medecin);
    }

    public void retirerMedecin(Medecin medecin){
        this.medecins.remove(medecin);
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