package com.fantasyhospital.model.creatures;

import com.fantasyhospital.model.creatures.abstractclass.Bete;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.servicemedical.ServiceMedical;

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

    public void transferer(Creature creature, ServiceMedical to) { /* ... */ }
} 