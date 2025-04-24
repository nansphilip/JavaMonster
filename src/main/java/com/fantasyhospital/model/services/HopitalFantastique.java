package com.fantasyhospital.model.services;

import java.util.ArrayList;
import java.util.List;

import com.fantasyhospital.model.creatures.Medecin;

public class HopitalFantastique {
    private String nom;
    private int capaciteMaxServices;
    private List<ServiceMedical> services = new ArrayList<>();
    private List<Medecin> medecins = new ArrayList<>();

    public HopitalFantastique(String nom, int capaciteMaxServices) {
        this.nom = nom;
        this.capaciteMaxServices = capaciteMaxServices;
    }

    public void afficherNombreCreatures() { /* ... */ }
    public void afficherToutesCreatures() { /* ... */ }
    public void simulation() { /* ... */ }

    // Getters et setters omis pour la clarté
} 