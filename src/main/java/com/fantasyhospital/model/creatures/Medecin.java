package com.fantasyhospital.model.creatures;

import com.fantasyhospital.model.creatures.abstractclass.Bete;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.salles.Salle;
import com.fantasyhospital.salles.servicemedical.ServiceMedical;

public class Medecin extends Bete {

    protected String race; //type du médecin, à voir si on créé une classe Race par exemple
    protected ServiceMedical serviceMedical;

    public Medecin(String nom, String sexe, int poids, int taille, int age, int moral, String race, ServiceMedical serviceMedical) {
        super(nom, sexe, poids, taille, age, moral);
        this.race = race;
        this.serviceMedical = serviceMedical;
    }

    @Override
    public void attendre() {
        //Attente du medecin si il a rien à faire par exemple
    }

    // Méthodes spécifiques : examiner, soigner, réviser budget, transférer créature
    public void examiner(ServiceMedical service) { /* ... */ }

    public void soigner(Creature creature) { /* ... */ }

    public void reviserBudget(int valeur) { /* ... */ }

    public void transferer(Creature creature, ServiceMedical serviceDestination) {
        if(!serviceDestination.getCreatures().isEmpty()) {
            String typeServiceDestination = serviceDestination.getCreatures().get(0).getClass().getSimpleName();
            if(!creature.getClass().getSimpleName().equals(typeServiceDestination)) {
                System.out.println("Transfert impossible, le service de destination n'est pas du bon type.");
                return;
            }
        }
        this.serviceMedical.enleverCreature(creature);
        serviceDestination.ajouterCreature(creature);
    }

    public void transferer(Creature creature,Salle salle, ServiceMedical serviceDestination) {
        if(!serviceDestination.getCreatures().isEmpty()) {
            String typeServiceDestination = serviceDestination.getCreatures().get(0).getClass().getSimpleName();
            if(!creature.getClass().getSimpleName().equals(typeServiceDestination)) {
                System.out.println("Transfert impossible, le service de destination n'est pas du bon type.");
                return;
            }
        }
        salle.enleverCreature(creature);
        serviceDestination.ajouterCreature(creature);
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public ServiceMedical getServiceMedical() {
        return serviceMedical;
    }

    public void setServiceMedical(ServiceMedical serviceMedical) {
        this.serviceMedical = serviceMedical;
    }

    @Override
    public String toString() {
        return "[Médecin] nom='" + nomComplet + "', sexe='" + sexe + "', âge=" + age + ", moral=" + moral + ", poids=" + poids + ", taille=" + taille + "]";
    }
} 