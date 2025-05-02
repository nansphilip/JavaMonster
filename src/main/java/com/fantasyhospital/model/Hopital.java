package com.fantasyhospital.model;

import com.fantasyhospital.model.creatures.Medecin;
import com.fantasyhospital.salles.servicemedical.ServiceMedical;

import java.util.ArrayList;
import java.util.List;

public class Hopital {
    private String nom;
    private final int NB_MAX_SERVICE;
    private List<ServiceMedical> services = new ArrayList<>();
    private List<Medecin> medecins = new ArrayList<>();

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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNB_MAX_SERVICE() {
        return NB_MAX_SERVICE;
    }

    public List<ServiceMedical> getServices() {
        return services;
    }

    public void setServices(List<ServiceMedical> services) {
        this.services = services;
    }
}