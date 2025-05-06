package com.fantasyhospital.model.creatures;

import com.fantasyhospital.model.creatures.abstractclass.Bete;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;
import com.fantasyhospital.salles.servicemedical.ServiceMedical;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter @Getter @Slf4j
public class Medecin extends Bete {

	//Getters and setters
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
            logger.error("[medecin][soigner()] La créature {} n'a pas de maladie",  creature.getNomComplet());
            return;
        }
        if(!creature.etreSoigne(maladie)){
            logger.error("[medecin][soigner()] La créature {} ne possédait pas la maladie {}", this.nomComplet, maladie.getNom());
        } else {
            logger.info("La maladie {} vient d'être soignée pour {} !", maladie.getNom(),  creature.getNomComplet());
        }
    }

    public void reviserBudget(int valeur) { /* ... */ }

    public boolean transferer(Creature creature,Salle salleFrom, Salle salleTo) {
        //Vérification que la creature est bien dans la salle
        CopyOnWriteArrayList<Creature> creaturesSalle = salleFrom.getCreatures();
        if(!creaturesSalle.contains(creature)) {
            logger.error("[medecin][transferer()] La créature {} à transférer n'est pas présente dans la salle {}.",  creature.getNomComplet(), salleFrom.getNom());
            return false;
        }
        CopyOnWriteArrayList<Creature> creaturesDest = salleTo.getCreatures();
        Iterator<Creature> iterator = creaturesSalle.iterator();
        if(!creaturesDest.isEmpty()) {
            Creature c1 = creaturesDest.get(0);
            String typeServiceDestination = iterator.next().getClass().getSimpleName();
            typeServiceDestination = c1.getRace();
            if(!creature.getRace().equals(typeServiceDestination)) {
                logger.error("[medecin][transferer()] Transfert impossible, le type du service de destination ({}) n'est pas du type de la créature ({}).", typeServiceDestination, creature.getClass().getSimpleName());
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

    //Getters and setters
    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public void depression(){
        this.moral = Math.max(this.moral - 40, 0);
        log.info("Dépression, médecin a maintenant {} de moral.", this.moral);
    }

    private void enFinir() {
        this.serviceMedical.retirerMedecin(this);
    }

	@Override
    public String toString() {
        return "[Médecin] nom='" + nomComplet + "', sexe='" + sexe + "', âge=" + age + ", moral=" + moral + ", poids=" + poids + ", taille=" + taille + "]";
    }
} 