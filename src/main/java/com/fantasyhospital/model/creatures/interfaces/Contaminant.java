package com.fantasyhospital.model.creatures.interfaces;

import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.model.rooms.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Interface of the contaminant creatures
 * Its particularity is that it can contaminate other creatures
 */
public interface Contaminant {

    Logger logger = LoggerFactory.getLogger(Contaminant.class);

    /**
     * Default method of the interface shared with all implementations
     * Contaminates a creature with a random disease it has in the list of diseases
     * If the creature has only one disease, it will lower the level of this disease to 5
     * @param CreatureDying the creature that is dying
     * @param room the room where the creature is dying
     */
    default void contaminate(Creature CreatureDying, Room room){
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
