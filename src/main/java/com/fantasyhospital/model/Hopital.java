package com.fantasyhospital.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fantasyhospital.model.creatures.Medecin;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.salles.Salle;

public class Hopital {
    private String nom;
    private final int NB_MAX_SERVICE;
    private List<Salle> services = new ArrayList<>();
    private List<Medecin> medecins = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(Hopital.class);

    public Hopital(String nom, int NB_MAX_SERVICE) {
        this.nom = nom;
        this.NB_MAX_SERVICE = NB_MAX_SERVICE;
    }

    public void afficherServices() {
        for(Salle salle : this.services) {
            logger.info("{}", salle);
        }
    }
    public void afficherNombreCreatures() { /* ... */ }

    public void afficherToutesCreatures() {
        for(Salle salle : this.services) {
            for(Creature creature : salle.getCreatures()){
                logger.info("{}", creature);
            }
        }
    }

    public void modifierRandomCreatures(){

    }

    public void modifierRandomServices(){

    }

    public void ajouterService(Salle salle){
        services.add(salle);
    }

    public void simulation() { /* ... */ }

    // Getters et setters omis pour la clarté
    public Salle getSalleOfCreature(Creature creature) {
        for(Salle salle : services) {
            for(Creature creatureSalles : salle.getCreatures()) {
                if(creatureSalles.equals(creature)) {
                    return salle;
                }
            }
        }
        return null;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNB_MAX_SERVICE() {
        return NB_MAX_SERVICE;
    }

    public List<Salle> getServices() {
        return this.services;
    }

    public void setServices(List<Salle> services) {
        this.services = services;
    }
}