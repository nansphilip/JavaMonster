package com.fantasyhospital.model.creatures;

import com.fantasyhospital.model.creatures.abstractclass.Bete;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;
import com.fantasyhospital.salles.servicemedical.ServiceMedical;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedHashSet;

public class Medecin extends Bete {

    protected String race; //type du médecin, à voir si on créé une classe Race par exemple
    protected ServiceMedical serviceMedical;
    private static final Logger logger = LoggerFactory.getLogger(Medecin.class);

    public Medecin(String nom, String sexe, int poids, int taille, int age, int moral, String race, ServiceMedical serviceMedical) {
        super(nom, sexe, poids, taille, age, moral);
        this.race = race;
        this.serviceMedical = serviceMedical;
    }

    @Override
    public void attendre() {
        //Attente du medecin si il a rien à faire par exemple
    }

    // Méthodes spécifiques : examiner, soigner, réviser budget, transférer créature
    public void examiner(ServiceMedical service) { /* ... */ }

    /** Soigne la maladie d'une créature avec le niveau le plus élevé **/
    public void soigner(Creature creature) {
        Maladie maladie = creature.getHighLevelMaladie();
        if(maladie == null){
            logger.info("La créature {} n'a pas de maladie",  creature.getNomComplet());
            return;
        }
        if(!creature.etreSoigne(maladie)){
            logger.info("La créature ne possédait pas cette maladie");
        } else {
            logger.info("La maladie {} vient d'être soignée pour {} !", maladie.getNomAbrege(),  creature.getNomComplet());
        }
    }

    public void reviserBudget(int valeur) { /* ... */ }

    public boolean transferer(Creature creature,Salle salleFrom, Salle salleTo) {
        //Vérification que la creature est bien dans la salle
        LinkedHashSet<Creature> creaturesSalle = salleFrom.getCreatures();
        if(!creaturesSalle.contains(creature)) {
            logger.info("La créature à transférer n'est pas présente dans la salle d'origine.");
            return false;
        }
        LinkedHashSet<Creature> creaturesDest = salleTo.getCreatures();
        Iterator<Creature> iterator = creaturesSalle.iterator();
        if(!creaturesDest.isEmpty()) {
            String typeServiceDestination = iterator.next().getClass().getSimpleName();
            if(!creature.getClass().getSimpleName().equals(typeServiceDestination)) {
                logger.info("Transfert impossible, le service de destination n'est pas du bon type.");
                return false;
            }
        }
        return salleFrom.enleverCreature(creature) && salleTo.ajouterCreature(creature);
    }

    public boolean verifierMoral(){
        if(this.moral==0) {
            enFinir();
            logger.info("Le médecin {} en a fini.", this);
            return false;
        }
        return true;
    }

    public void depression(){
        this.moral = Math.max(this.moral - 40, 0);
        logger.info("Dépression, médecin a maintenant {} de moral.", this.moral);
    }

    private void enFinir() {
        this.serviceMedical.retirerMedecin(this);
    }

    //Getters and setters
    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public ServiceMedical getServiceMedical() {
        return serviceMedical;
    }

    public void setServiceMedical(ServiceMedical serviceMedical) {
        this.serviceMedical = serviceMedical;
    }

    @Override
    public String toString() {
        return "[Médecin] nom='" + nomComplet + "', sexe='" + sexe + "', âge=" + age + ", moral=" + moral + ", poids=" + poids + ", taille=" + taille + "]";
    }
} 