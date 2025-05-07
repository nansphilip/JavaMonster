package com.fantasyhospital.salles.servicemedical;

import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.Medecin;
import com.fantasyhospital.salles.Salle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter public class ServiceMedical extends Salle  {
	// Getters et setters omis pour la clarté
	protected List<Medecin> medecins = new ArrayList<>();
    protected String budget; // inexistant, médiocre, insuffisant, faible

    public ServiceMedical(String nom, double superficie, int NB_MAX_CREATURE, String budget) {
        super(nom, superficie, NB_MAX_CREATURE);
        this.budget = budget;
    }

    @Override
    public boolean ajouterCreature(Creature creature){
        if (creatures.size() >= NB_MAX_CREATURE) {
            return false;
        }

        if (creatures.isEmpty()) {
            creatures.add(creature);
            return true;
        }

        Iterator<Creature> iterator = creatures.iterator();
        String raceAutorisee = iterator.next().getRace(); //.getFirst().getRace();
        if (creature.getRace().equals(raceAutorisee)) {
            creatures.add(creature);
            return true;
        }

        return false;
    }

    public void ajouterMedecin(Medecin medecin){
        this.medecins.add(medecin);
    }

    public void retirerMedecin(Medecin medecin){
        this.medecins.remove(medecin);
    }

    public void soignerCreatures(Medecin medecin, Creature creature){

    }

    public void reviserBudget(int valeur){

    }

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