package com.fantasyhospital.model;

import java.util.ArrayList;
import java.util.List;

import com.fantasyhospital.model.creatures.Medecin;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.salles.Salle;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Représente l'hôpital Fantasy Hospital.
 * Gère la liste des services (salles), les médecins et les opérations globales sur l'hôpital.
 */
@Slf4j
public class Hospital {

    /**
     * Nom de l'hôpital
     */
    @Setter
    @Getter
    private String nom;

    /**
     * Nombre maximal de services que l'hôpital peut contenir
     */
    @Getter
    private final int NB_MAX_SERVICE;

    /**
     * Liste des services (salles) de l'hôpital
     */
    @Setter
    @Getter
    private List<Salle> services = new ArrayList<Salle>();

    /**
     * Liste des médecins de l'hôpital
     */
    private List<Medecin> medecins = new ArrayList<>();

    /**
     * Crée un nouvel hôpital.
     *
     * @param nom nom de l'hôpital
     * @param NB_MAX_SERVICE nombre maximal de services
     */
    public Hospital(String nom, int NB_MAX_SERVICE) {
        this.nom = nom;
        this.NB_MAX_SERVICE = NB_MAX_SERVICE;
    }

    /**
     * Affiche les informations de chaque service de l'hôpital dans les logs.
     */
    public void afficherServices() {
        for (Salle salle : this.services) {
            log.info("{}", salle);
        }
    }

    /**
     * Affiche le nombre total de créatures dans l'hôpital (à compléter).
     */
    public void afficherNombreCreatures() {
        /* ... */ }

    /**
     * Affiche toutes les créatures présentes dans tous les services de
     * l'hôpital.
     */
    public void afficherToutesCreatures() {
        for (Salle salle : this.services) {
            for (Creature creature : salle.getCreatures()) {
                log.info("{}", creature);
            }
        }
    }

    /**
     * Modifie aléatoirement des créatures (à compléter).
     */
    public void modifierRandomCreatures() {

    }

    /**
     * Modifie aléatoirement des services (à compléter).
     */
    public void modifierRandomServices() {

    }

    /**
     * Ajoute un service (salle) à l'hôpital.
     *
     * @param salle la salle à ajouter
     */
    public void ajouterService(Salle salle) {
        services.add(salle);
    }

    /**
     * Lance la simulation de l'hôpital (à compléter).
     */
    public void simulation() {
    }

    /**
     * Retourne la salle dans laquelle se trouve une créature donnée.
     *
     * @param creature la créature recherchée
     * @return la salle contenant la créature, ou null si absente
     */
    public Salle getSalleOfCreature(Creature creature) {
        for (Salle salle : services) {
            for (Creature creatureSalles : salle.getCreatures()) {
                if (creatureSalles.equals(creature)) {
                    return salle;
                }
            }
        }
        return null;
    }

}
