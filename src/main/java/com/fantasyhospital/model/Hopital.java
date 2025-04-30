package com.fantasyhospital.model;

import com.fantasyhospital.model.creatures.Medecin;

import java.util.ArrayList;
import java.util.List;

public class Hopital {
    private String nom;
    private int capaciteMaxServices;
    private List<Service> services = new ArrayList<>();
    private List<Medecin> medecins = new ArrayList<>();

    public Hopital(String nom, int capaciteMaxServices) {
        this.nom = nom;
        this.capaciteMaxServices = capaciteMaxServices;
    }

    public void afficherNombreCreatures() { /* ... */ }
    public void afficherToutesCreatures() { /* ... */ }
    public void simulation() { /* ... */ }

    // Getters et setters omis pour la clarté
} 