package com.fantasyhospital.model.creatures.interfaces;

import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.CopyOnWriteArrayList;

public interface Contaminant {

    static final Logger logger = LoggerFactory.getLogger(Contaminant.class);

    default void contaminer(Creature creatureMourrante, Salle salle){
        //Body de la mtéthode contaminer commune à toutes les classes de l'interface
        //On récupère une maladie au hasard, mais qui n'est pas au niveau max si possible
        CopyOnWriteArrayList<Maladie> maladies = creatureMourrante.getMaladies();
        Maladie maladie = creatureMourrante.getRandomMaladie();
        for(Maladie m : maladies){
            if(!m.estLethale()){
                maladie = m;
                break;
            }
        }
        Creature creature = salle.getRandomCreatureWithoutThisOne(creatureMourrante);
        if(creature == null){
            logger.error("Il n'y avait aucune créature à contaminer dans la salle {}", salle.getNom());
            return;
        }
        creature.tomberMalade(maladie);
        logger.info("La créature {} vient d'être contaminée par la maladie {} que portait {}.", creature.getNomComplet(), maladie.getNom(), creatureMourrante.getNomComplet());
    }
} 
