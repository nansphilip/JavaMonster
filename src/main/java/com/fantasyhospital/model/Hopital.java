package com.fantasyhospital.model;

import com.fantasyhospital.model.creatures.Medecin;
import com.fantasyhospital.model.servicemedical.ServiceMedical;

import java.util.ArrayList;
import java.util.List;

public class Hopital {
    private String nom;
    private final int NB_MAX_SERVICE;
    private List<ServiceMedical> services = new ArrayList<>();

    public Hopital(String nom, int NB_MAX_SERVICE) {
        this.nom = nom;
        this.NB_MAX_SERVICE = NB_MAX_SERVICE;
    }

    public void afficherNombreCreatures() { /* ... */ }

    public void afficherToutesCreatures() { /* ... */ }

    public void modifierRandomCreatures(){

    }

    public void modifierRandomServices(){

    }

    public void ajouterService(ServiceMedical service){
        services.add(service);
    }

    public void simulation() { /* ... */ }

    // Getters et setters omis pour la clarté
} 