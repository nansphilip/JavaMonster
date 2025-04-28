package com.fantasyhospital.model;

public class Medecin extends Creature {
    public Medecin(String nom, String sexe, double poids, double taille, int age) {
        super(nom, sexe, poids, taille, age);
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