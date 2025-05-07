package com.fantasyhospital.model;

import java.util.ArrayList;
import java.util.List;

import com.fantasyhospital.model.creatures.Medecin;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.salles.Salle;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Hopital {
    @Setter @Getter private String nom;
    @Getter private final int NB_MAX_SERVICE;
    @Setter @Getter  private List<Salle> services = new ArrayList<Salle>();
    private List<Medecin> medecins = new ArrayList<>();

    public Hopital(String nom, int NB_MAX_SERVICE) {
        this.nom = nom;
        this.NB_MAX_SERVICE = NB_MAX_SERVICE;
    }

    public void afficherServices() {
        for(Salle salle : this.services) {
            log.info("{}", salle);
        }
    }
    public void afficherNombreCreatures() { /* ... */ }

    public void afficherToutesCreatures() {
        for(Salle salle : this.services) {
            for(Creature creature : salle.getCreatures()){
                log.info("{}", creature);
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

}