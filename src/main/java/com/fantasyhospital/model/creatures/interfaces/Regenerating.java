package com.fantasyhospital.model.creatures.interfaces;

import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.disease.Disease;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Regenerating {

    Logger logger = LoggerFactory.getLogger(Regenerating.class);

    default boolean regenerate(Creature creature) {
        //Supprime disease qui l'a tué
        Disease disease = creature.getHighLevelDisease();
        creature.getDiseases().remove(disease);
        //Regénère avec les diseases qu'il avait
        if(!creature.getDiseases().isEmpty()) {
            logger.info("La créature {} revient à la vie, alleluia !!", creature.getFullName());
            return false;
        }
        //si il avait 1 seule disease, 50% chance attraper autre disease ou sortir hopital
        if(Math.random() > 0.5){
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

    default boolean isCreatureDeadButWillRevive(Creature creature) {
        for(Disease disease : creature.getDiseases()){
            if(disease.isLethal()){
                return true;
            }
        }
        return creature.getDiseases().size() >= 4;
    }

} 