package com.fantasyhospital.model.creatures.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.rooms.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Demoralizing {

    Logger logger = LoggerFactory.getLogger(Demoralizing.class);

    default void demoralize(Creature CreatureDying, Room room){
        //Body de la mtéthode contaminer commune à toutes les classes de l'interface
        List<Creature> creatures = room.getCreatures();
        Random random = new Random();
        int morale = 0;
        switch(creatures.size()){
            case 1:
                logger.error("Aunce créature à démoraliser, la créature {} était seule dans la room {}.",  CreatureDying.getFullName(), room.getName());
                break;
            case 2:
                Creature c = room.getRandomCreatureWithoutThisOne(CreatureDying);
                morale = Math.max(c.getMorale() - 10, 0);
                c.setMorale(morale);
                logger.info("La créature {} a été démoralisée.", c.getFullName());
                break;
            default:
                Creature c1 = room.getRandomCreatureWithoutThisOne(CreatureDying);
                List<Creature> creaturesToExclude = new ArrayList<>();
                creaturesToExclude.add(c1);
                creaturesToExclude.add(CreatureDying);
                Creature c2 = room.getRandomCreatureWithoutThem(creaturesToExclude);
                morale = Math.max(c1.getMorale() - 10, 0);
                c1.setMorale(morale);
                morale = Math.max(c2.getMorale() - 10, 0);
                c2.setMorale(morale);
                logger.info("Les créatures {} et {} ont été démoralisés par {}.", c1.getFullName(), c2.getFullName(), CreatureDying.getFullName());
        }
    }
}