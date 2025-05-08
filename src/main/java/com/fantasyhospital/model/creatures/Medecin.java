package com.fantasyhospital.model.creatures;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fantasyhospital.enums.ActionType;
import com.fantasyhospital.model.creatures.abstractclass.Bete;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;
import com.fantasyhospital.salles.servicemedical.ServiceMedical;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Représente un médecin dans Fantasy Hospital. Un médecin peut soigner,
 * transférer des créatures, réviser le budget et gérer son propre moral.
 */
@Setter
@Getter
@Slf4j
public class Medecin extends Bete {

    /**
     * Race/type du médecin (ex: Lycanthrope)
     */
    @Setter
    @Getter
    protected String race;

    /**
     * Service médical auquel le médecin est affecté
     */
    protected ServiceMedical serviceMedical;

    /**
     * Construit un médecin avec ses caractéristiques et son service
     * d'affectation.
     */
    public Medecin(String nom, String sexe, int poids, int taille, int age, int moral, String race, ServiceMedical serviceMedical) {
        super(nom, sexe, poids, taille, age, moral);
        this.race = race;
        this.serviceMedical = serviceMedical;
    }

    /**
     * Action d'attente du médecin (à compléter si besoin).
     */
    @Override
    public void attendre(Salle salle) {

    }

    /**
     * Examine un service médical (à compléter).
     */
    public void examiner(ServiceMedical service) {
    }

    /**
     * Soigne la maladie d'une créature avec le niveau le plus élevé.
     *
     * @param creature la créature à soigner
     */
    public void soigner(Creature creature) {
        Maladie maladie = creature.getHighLevelMaladie();
        if (maladie == null) {
            log.error("[medecin][soigner()] La créature {} n'a pas de maladie", creature.getNomComplet());
            return;
        }
        if (!creature.etreSoigne(maladie)) {
            log.error("[medecin][soigner()] La créature {} ne possédait pas la maladie {}", this.nomComplet, maladie.getNom());
        } else {
            log.info("La maladie {} vient d'être soignée pour {} !", maladie.getNom(), creature.getNomComplet());
            int soigne = ActionType.MEDECIN_SOIGNE.getVariationMoral();
            this.moral = Math.min(this.moral + soigne, 100);
            log.info("Soigner a redonné {} points de moral. Moral actuel : {}", soigne, this.moral);
        }
    }

    /**
     * Révise le budget du service médical (à compléter).
     */
    public void reviserBudget(int valeur) {
    }

    /**
     * Transfère une créature d'une salle à une autre si les conditions sont
     * réunies.
     *
     * @param creature la créature à transférer
     * @param salleFrom salle de départ
     * @param salleTo salle d'arrivée
     * @return true si le transfert a réussi, false sinon
     */
    public boolean transferer(Creature creature, Salle salleFrom, Salle salleTo) {
        // Vérification que la créature est bien dans la salle
        CopyOnWriteArrayList<Creature> creaturesSalle = salleFrom.getCreatures();
        if (!creaturesSalle.contains(creature)) {
            log.error("[medecin][transferer()] La créature {} à transférer n'est pas présente dans la salle {}.", creature.getNomComplet(), salleFrom.getNom());
            return false;
        }
        CopyOnWriteArrayList<Creature> creaturesDest = salleTo.getCreatures();
        Iterator<Creature> iterator = creaturesSalle.iterator();
        if (!creaturesDest.isEmpty()) {
            Creature c1 = creaturesDest.get(0);
            String typeServiceDestination = iterator.next().getClass().getSimpleName();
            typeServiceDestination = c1.getRace();
            if (!creature.getRace().equals(typeServiceDestination)) {
                log.error("[medecin][transferer()] Transfert impossible, le type du service de destination ({}) n'est pas du type de la créature ({}).", typeServiceDestination, creature.getClass().getSimpleName());
                return false;
            }
        }
        return salleFrom.enleverCreature(creature) && salleTo.ajouterCreature(creature);
    }

    /**
     * Vérifie le moral du médecin. Si à zéro, il quitte le service.
     *
     * @return false si le médecin a quitté, true sinon
     */
    public boolean verifierMoral() {
        if (this.moral == 0) {
            enFinir();
            log.info("Le médecin {} en a fini.", this);
            return false;
        }
        return true;
    }

    /**
     * Applique une dépression au médecin (baisse de moral).
     */
    public void depression() {
        int depression = ActionType.MEDECIN_DEPRESSION.getVariationMoral();
        this.moral = Math.max(this.moral + depression, 0);
        log.info("Dépression, médecin a maintenant {} de moral.", this.moral);
    }

    /**
     * Retire le médecin de son service médical.
     */
    private void enFinir() {
        this.serviceMedical.retirerMedecin(this);
    }

    @Override
    public String toString() {
        return "[Médecin] nom='" + nomComplet + "', sexe='" + sexe + "', âge=" + age + ", moral=" + moral + ", poids=" + poids + ", taille=" + taille + "]";
    }
}
