package com.fantasyhospital.salles.servicemedical;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fantasyhospital.model.creatures.Medecin;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.salles.Salle;

import lombok.Getter;
import lombok.Setter;

/**
 * Représente un service médical spécialisé dans l'hôpital Fantasy Hospital.
 * Hérite de Salle et ajoute la gestion des médecins et du budget.
 */
@Setter
@Getter
public class ServiceMedical extends Salle {

    /**
     * Liste des médecins affectés à ce service
     */
    protected List<Medecin> medecins = new ArrayList<>();

    /**
     * Budget du service (ex: inexistant, médiocre, insuffisant, faible)
     */
    protected String budget;

    /**
     * Crée un service médical avec nom, superficie, capacité et budget.
     */
    public ServiceMedical(String nom, double superficie, int NB_MAX_CREATURE, String budget) {
        super(nom, superficie, NB_MAX_CREATURE);
        this.budget = budget;
    }

    /**
     * Ajoute une créature au service médical si la capacité et la race sont
     * compatibles.
     */
    @Override
    public boolean ajouterCreature(Creature creature) {
        if (creatures.size() >= NB_MAX_CREATURE) {
            return false;
        }
        if (creatures.isEmpty()) {
            creatures.add(creature);
            return true;
        }
        Iterator<Creature> iterator = creatures.iterator();
        String raceAutorisee = iterator.next().getRace();
        if (creature.getRace().equals(raceAutorisee)) {
            creatures.add(creature);
            return true;
        }
        return false;
    }

    /**
     * Ajoute un médecin à la liste des médecins du service.
     */
    public void ajouterMedecin(Medecin medecin) {
        this.medecins.add(medecin);
        medecin.setServiceMedical(this);
    }

    /**
     * Retire un médecin du service.
     */
    public void retirerMedecin(Medecin medecin) {
        this.medecins.remove(medecin);
        medecin.setServiceMedical(null);
    }

    /**
     * Soigne une créature par un médecin (à compléter).
     */
    public void soignerCreatures(Medecin medecin, Creature creature) {

    }

    /**
     * Révise le budget du service (à compléter).
     */
    public void reviserBudget(int valeur) {

    }

    /**
     * Retourne une représentation textuelle du service médical, de ses médecins
     * et de ses créatures.
     */
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
