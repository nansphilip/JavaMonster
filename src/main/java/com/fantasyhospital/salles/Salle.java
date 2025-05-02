package com.fantasyhospital.salles;

import com.fantasyhospital.model.creatures.abstractclass.Creature;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Salle {
    private static final Logger logger = LoggerFactory.getLogger(Salle.class);

    protected String nom;
    protected double superficie;
    protected final int NB_MAX_CREATURE;
    protected List<Creature> creatures = new ArrayList<>();

    public Salle(String nom, double superficie, int NB_MAX_CREATURE) {
        this.nom = nom;
        this.superficie = superficie;
        this.NB_MAX_CREATURE = NB_MAX_CREATURE;
    }

    public boolean ajouterCreature(Creature creature){
        if (creatures.size() >= NB_MAX_CREATURE) {
            return false;
        } else {
            creatures.add(creature);
            return true;
        }
    }

    public void enleverCreature(Creature creature){
        this.creatures.remove(creature);
    }

    public void afficherInfosService(){
        logger.info("\n{}", this);
    }

    public void afficherInfosCreatures(){

    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getSuperficie() {
        return superficie;
    }

    public void setSuperficie(double superficie) {
        this.superficie = superficie;
    }

    public int getNB_MAX_CREATURE() {
        return NB_MAX_CREATURE;
    }

    public List<Creature> getCreatures() {
        return creatures;
    }

    public void setCreatures(List<Creature> creatures) {
        this.creatures = creatures;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Salle : ").append(nom).append(" ---\n");
        sb.append("Superficie : ").append(superficie).append(" m²\n");
        sb.append("Nombre de créatures maximale : ").append(NB_MAX_CREATURE).append("\n");

        sb.append("\n🏥 Salle :\n");
        if (creatures.isEmpty()) {
            sb.append("  Aucune créature dans ce service.\n");
        } else {
            for (Creature c : creatures) {
                sb.append("  - ").append(c).append("\n");
            }
        }

        return sb.toString();
    }
}
