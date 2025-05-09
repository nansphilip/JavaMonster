package com.fantasyhospital.model.creatures.interfaces;

import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.maladie.Maladie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Regenerant {

    static final Logger logger = LoggerFactory.getLogger(Regenerant.class);

    default boolean regenerer(Creature creature) {
        //Supprime maladie qui l'a tué
        Maladie maladie = creature.getHighLevelMaladie();
        creature.getMaladies().remove(maladie);
        //Regénère avec les maladies qu'il avait
        if(!creature.getMaladies().isEmpty()) {
            logger.info("La créature {} revient à la vie, alleluia !!", creature.getNomComplet());
            return false;
        }
        //si il avait 1 seule maladie, 50% chance attraper autre maladie ou sortir hopital
        if(Math.random() > 0.5){
            maladie = new Maladie();
            creature.getMaladies().add(maladie);
            logger.info("La créature {} revient à la vie, elle contracte la maladie {} en regénérant.", creature.getNomComplet(), maladie.getNom());
            return false;
        } else {
            //Creature sort de l'hopital
            logger.info("La créature {} revient à la vie, et sort de l'hopital puisqu'elle n'est plus malade !",  creature.getNomComplet());
            return true;
        }
    }

    default boolean isCreatureDeadButWillRevive(Creature creature) {
        for(Maladie maladie : creature.getMaladies()){
            if(maladie.estLethale()){
                return true;
            }
        }
        return creature.getMaladies().size() >= 4;
    }

} 