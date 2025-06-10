package com.fantasyhospital.model.creatures.interfaces;

import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Interface for the creatures that can regenerate
 */
public interface Regenerating {

    Logger logger = LoggerFactory.getLogger(Regenerating.class);

    //Constants
    public static final double BE_HEALED_CHANCE = 0.5;

    /**
     * Make the creature regenerate when it dies
     * If it had only one disease, it has 50% to leave the hospital cured, or to get sick by a random disease
     * @param creature the creature dying
     * @return true if it has to get out of the hospital, false otherwise
     */
    default boolean regenerate(Creature creature) {
        //Supprime disease qui l'a tué pour ne pas boucler infiniment
        Disease disease = creature.getHighLevelDisease();
        creature.getDiseases().remove(disease);
        //Regénère avec les diseases qu'il avait
        if(!creature.getDiseases().isEmpty()) {
            logger.info("La créature {} revient à la vie, alleluia !!", creature.getFullName());
            return false;
        }
        //si il avait 1 seule disease, 50% chance attraper autre disease ou sortir hopital
        if(Math.random() < BE_HEALED_CHANCE) {
            disease = new Disease();
            creature.getDiseases().add(disease);
            logger.info("La créature {} revient à la vie, elle contracte la disease {} en regénérant.", creature.getFullName(), disease.getName());
            return false;
        } else {
            //Creature sort de l'hopital
            logger.info("La créature {} revient à la vie, et sort de l'hopital puisqu'elle n'est plus malade !",  creature.getFullName());
            return true;
        }
    }

    /**
     * Method that helps to know if the creature is about to die due to its diseases
     * @param creature the creature to check
     * @return boolean true if it will die, false otherwise
     */
    default boolean isCreatureDeadButWillRevive(Creature creature) {
        for(Disease disease : creature.getDiseases()){
            if(disease.isLethal()){
                return true;
            }
        }
        return creature.getDiseases().size() >= 4;
    }

    /**
     * Special method to cure a creature in the crypt (when it waits 3 tours with a temperature enough cool
     * @param creature the creature to cure
     * @param medicalService the crypt
     */
    default void cureCreatureInCrypt(Creature creature, MedicalService medicalService) {
        CopyOnWriteArrayList<Disease> list = new CopyOnWriteArrayList<>(creature.getDiseases());
        for(Disease disease : list){
            creature.beCured(disease, medicalService);
        }
    }
} 