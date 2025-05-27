package com.fantasyhospital.model.creatures.interfaces;

import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.model.rooms.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.CopyOnWriteArrayList;

public interface Contaminant {

    Logger logger = LoggerFactory.getLogger(Contaminant.class);

    default void contaminate(Creature CreatureDying, Room room){
        //Body de la mtéthode contaminer commune à toutes les classes de l'interface
        //On récupère une disease au hasard, mais qui n'est pas au niveau max
        //Si il n'en avait qu'une, on baisse le niveau de la maladie à 5
        CopyOnWriteArrayList<Disease> diseases = CreatureDying.getDiseases();
        Disease disease = CreatureDying.getRandomDisease();
        for(Disease m : diseases){
            if(!m.isLethal()){
                disease = m;
                break;
            }
        }
        if(disease.isLethal()){
            disease.setCurrentLevel(5);
        }
        Creature creature = room.getRandomCreatureWithoutThisOne(CreatureDying);
        if(creature == null){
            logger.error("Il n'y avait aucune créature à contaminer dans la room {}", room.getName());
            return;
        }
        creature.fallSick(disease);
        logger.info("La créature {} vient d'être contaminée par la disease {} que portait {}.", creature.getFullName(), disease.getName(), CreatureDying.getFullName());
    }
} 
