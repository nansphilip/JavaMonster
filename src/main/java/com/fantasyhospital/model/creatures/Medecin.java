package com.fantasyhospital.model.creatures;

import com.fantasyhospital.model.Creature;
import com.fantasyhospital.model.maladie.Maladie;

public class Medecin extends Bete {

    protected Race race;

    public Medecin(String nomComplet, String sexe, double poids, double taille, int age, int moral) {
        super(nom, sexe, poids, taille, age, moral);
        this.race = race;
    }

    @Override
    public void attendre() { /* ... */ }
    @Override
    public void hurler() { /* ... */ }
    @Override
    public void semporter() { /* ... */ }
    @Override
    public void tomberMalade(Maladie maladie) {
        // Un médecin ne peut pas tomber malade
    }
    @Override
    public void soigner() { /* ... */ }
    @Override
    public void trepasser() { /* ... */ }

    // Méthodes spécifiques : examiner, soigner, réviser budget, transférer créature
    public void examiner(Service service) { /* ... */ }
    public void soignerService(Service service) { /* ... */ }
    public void reviserBudget(Service service) { /* ... */ }
    public void transferer(Creature creature, Service from, Service to) { /* ... */ }
} 