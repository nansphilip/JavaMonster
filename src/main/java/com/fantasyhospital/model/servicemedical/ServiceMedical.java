package com.fantasyhospital.model.servicemedical;

import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.Medecin;

import java.util.ArrayList;
import java.util.List;

public class ServiceMedical {
    protected String nom;
    protected double superficie;
    protected final int NB_MAX_CREATURE;
    protected List<Creature> creatures = new ArrayList<>();
    protected List<Medecin> medecins = new ArrayList<>();
    protected String budget; // inexistant, médiocre, insuffisant, faible

    public ServiceMedical(String nom, double superficie, int NB_MAX_CREATURE, String budget) {
        this.nom = nom;
        this.superficie = superficie;
        this.NB_MAX_CREATURE = NB_MAX_CREATURE;
        this.budget = budget;
    }

    public void afficherInfosService(){
        System.out.println(this);
    }

    public void afficherInfosCreatures(){

    }

    public void ajouterMedecin(Medecin medecin){
        this.medecins.add(medecin);
    }

    public void retirerMedecin(Medecin medecin){
        this.medecins.remove(medecin);
    }

    public boolean ajouterCreature(Creature creature){
        if (creatures.size() >= NB_MAX_CREATURE) {
            return false;
        }

        if (creatures.isEmpty()) {
            creatures.add(creature);
            return true;
        }

        String raceAutorisee = creatures.get(0).getRace();
        if (creature.getRace().equals(raceAutorisee)) {
            creatures.add(creature);
            return true;
        }

        return false;
    }

    public boolean enleverCreature(Creature creature){
        return false;
    }

    public void soignerCreatures(Medecin medecin, Creature creature){

    }

    public void reviserBudget(int valeur){

    }

    // Getters et setters omis pour la clarté

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Service : ").append(nom).append(" ---\n");
        sb.append("Superficie : ").append(superficie).append(" m²\n");
        sb.append("Nombre de créatures maximale : ").append(NB_MAX_CREATURE).append("\n");
        sb.append("Budget : ").append(budget).append("\n");

        sb.append("\n🧍 Médecins :\n");
        if (medecins.isEmpty()) {
            sb.append("  Aucun médecin dans ce service.\n");
        } else {
            for (Medecin m : medecins) {
                sb.append("  - ").append(m.toString()).append("\n");
            }
        }

        sb.append("\n👾 Créatures :\n");
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