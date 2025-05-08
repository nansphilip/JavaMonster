package com.fantasyhospital.model.creatures;

import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.abstractclass.Creature;

/**
 * Thread chargé de surveiller et de gérer le moral d'une créature dans l'hôpital.
 *
 * À chaque itération, il vérifie le moral et la santé de la créature.
 * Si la créature décède ou quitte la salle, elle est retirée de l'hôpital.
 */
public class MoralThread implements Runnable {

    /**
     * La créature surveillée par ce thread
     */
    private Creature creature;

    /**
     * L'hôpital dans lequel se trouve la créature
     */
    private Hospital hospital;

    /**
     * Construit un thread de gestion du moral pour une créature donnée.
     *
     * @param creature la créature à surveiller
     * @param hospital l'hôpital de référence
     */
    public MoralThread(Creature creature, Hospital hospital) {
        this.creature = creature;
        this.hospital = hospital;
    }

    @Override
    public void run() {
        // Boucle tant que la créature est présente dans une salle
        while (hospital.getSalleOfCreature(creature) != null) {
            // Vérifie le moral de la créature dans sa salle actuelle
            this.creature.verifierMoral(this.hospital.getSalleOfCreature(creature));

            // Si la créature décède ou doit quitter la salle
            if (this.creature.verifierSante(this.hospital.getSalleOfCreature(creature))) {
                this.hospital.getSalleOfCreature(creature).enleverCreature(this.creature);
                try {
                    Thread.sleep(200); // Petite pause avant de mettre à jour l'affichage
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                this.hospital.afficherToutesCreatures();
                break;
            }
        }
    }
}
