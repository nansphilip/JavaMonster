package com.fantasyhospital.model.creatures.interfaces;

import com.fantasyhospital.enums.ActionType;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.rooms.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Interface for the creatures that can demoralize other creatures
 */
public interface Demoralizing {

    Logger logger = LoggerFactory.getLogger(Demoralizing.class);

    /**
     * Demoralize a maximum of two creatures in the room when a creature dies
     * The morale of the other creatures is decreased by 10 points
     * If there is only one creature, nothing happens
     * If there were two creatures, the other one is demoralized
     * If there were more than two creatures, two random creatures are demoralized
     * @param CreatureDying the creature that is dying
     * @param room the room where the creature is dying
     */
    default void demoralize(Creature CreatureDying, Room room){
        List<Creature> creatures = room.getCreatures();
        Random random = new Random();
        int morale = 0;
        switch(creatures.size()){
            case 1:
                logger.error("Aunce créature à démoraliser, la créature {} était seule dans la room {}.",  CreatureDying.getFullName(), room.getName());
                break;
            case 2:
                Creature c = room.getRandomCreatureWithoutThisOne(CreatureDying);
                morale = Math.max(c.getMorale() + ActionType.CREATURE_DEMORALIZED.getMoraleVariation(), 0);
                c.setMorale(morale);
                logger.info("La créature {} a été démoralisée.", c.getFullName());
                break;
            default:
                Creature c1 = room.getRandomCreatureWithoutThisOne(CreatureDying);
                List<Creature> creaturesToExclude = new ArrayList<>();
                creaturesToExclude.add(c1);
                creaturesToExclude.add(CreatureDying);
                Creature c2 = room.getRandomCreatureWithoutThem(creaturesToExclude);
                morale = Math.max(c1.getMorale() + ActionType.CREATURE_DEMORALIZED.getMoraleVariation(), 0);
                c1.setMorale(morale);
                morale = Math.max(c2.getMorale() + ActionType.CREATURE_DEMORALIZED.getMoraleVariation(), 0);
                c2.setMorale(morale);
                logger.info("Les créatures {} et {} ont été démoralisés par {}.", c1.getFullName(), c2.getFullName(), CreatureDying.getFullName());
        }
    }
}