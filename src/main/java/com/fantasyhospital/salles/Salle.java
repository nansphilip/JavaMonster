package com.fantasyhospital.salles;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fantasyhospital.model.creatures.abstractclass.Creature;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Représente une salle générique dans l'hôpital Fantasy Hospital.
 * Une salle contient un ensemble de créatures et possède un nom, une superficie et une capacité maximale.
 * Elle offre des méthodes pour ajouter, retirer et rechercher des créatures.
 */
@Getter
@Slf4j
public class Salle {

    /**
     * Nom de la salle
     */
    @Setter
    protected String nom;

    /**
     * Superficie de la salle en m²
     */
    @Setter
    protected double superficie;

    /**
     * Nombre maximal de créatures pouvant être accueillies
     */
    protected final int NB_MAX_CREATURE;

    /**
     * Liste des créatures présentes dans la salle (thread-safe)
     */
    @Setter
    @Getter
    protected CopyOnWriteArrayList<Creature> creatures = new CopyOnWriteArrayList<>();

    /**
     * Crée une nouvelle salle.
     *
     * @param nom nom de la salle
     * @param superficie superficie en m²
     * @param NB_MAX_CREATURE capacité maximale
     */
    public Salle(String nom, double superficie, int NB_MAX_CREATURE) {
        this.nom = nom;
        this.superficie = superficie;
        this.NB_MAX_CREATURE = NB_MAX_CREATURE;
    }

    /**
     * Ajoute une créature à la salle si la capacité n'est pas atteinte.
     *
     * @param creature la créature à ajouter
     * @return true si ajoutée, false sinon
     */
    public boolean ajouterCreature(Creature creature) {
        if (creatures.size() >= NB_MAX_CREATURE) {
            return false;
        } else {
            creatures.add(creature);
            return true;
        }
    }

    /**
     * Retire une créature de la salle.
     *
     * @param creature la créature à retirer
     * @return true si retirée, false sinon
     */
    public boolean enleverCreature(Creature creature) {
        return this.creatures.remove(creature);
    }

    /**
     * Affiche les informations de la salle dans les logs.
     */
    public void afficherInfosService() {
        log.info("\n{}", this);
    }

    /**
     * Affiche les informations des créatures (à compléter).
     */
    public void afficherInfosCreatures() {

    }

    /**
     * Retourne la première créature de la salle.
     */
    public Creature getFirstCreature() {
        return creatures.iterator().next();
    }

    /**
     * Retourne la dernière créature de la salle.
     */
    public Creature getLastCreature() {
        return (Creature) creatures.toArray()[creatures.size() - 1];
    }

    /**
     * Recherche une créature par son nom complet.
     *
     * @param creatureName nom complet de la créature
     * @return la créature trouvée ou null
     */
    public Creature getCreatureByName(String creatureName) {
        for (Creature creature : creatures) {
            if (creature.getNomComplet().equals(creatureName)) {
                return creature;
            }
        }
        return null;
    }

    /**
     * Retourne une créature aléatoire parmi celles présentes dans la salle.
     */
    public Creature getRandomCreature() {
        Random random = new Random();
        return (Creature) this.creatures.toArray()[random.nextInt(this.creatures.size())];
    }

    /**
     * Retourne une représentation textuelle de la salle et de ses créatures.
     */
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
