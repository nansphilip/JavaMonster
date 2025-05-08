package com.fantasyhospital.model.creatures.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.salles.Salle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Demoralisant {

    static final Logger logger = LoggerFactory.getLogger(Demoralisant.class);

    default void demoraliser(Creature creatureMourrante, Salle salle){
        //Body de la mtéthode contaminer commune à toutes les classes de l'interface
        List<Creature> creatures = salle.getCreatures();
        Random random = new Random();
        int moral = 0;
        switch(creatures.size()){
            case 1:
                logger.error("Aunce créature à démoraliser, la créature {} était seule dans la salle {}.",  creatureMourrante.getNomComplet(), salle.getNom());
                break;
            case 2:
                Creature c = salle.getRandomCreatureWithoutThisOne(creatureMourrante);
                moral = Math.max(c.getMoral() - 10, 0);
                c.setMoral(moral);
                logger.info("La créature {} a été démoralisée.", c.getNomComplet());
                break;
            default:
                Creature c1 = salle.getRandomCreatureWithoutThisOne(creatureMourrante);
                List<Creature> creaturesToExclude = new ArrayList<>();
                creaturesToExclude.add(c1);
                creaturesToExclude.add(creatureMourrante);
                Creature c2 = salle.getRandomCreatureWithoutThem(creaturesToExclude);
                moral = Math.max(c1.getMoral() - 10, 0);
                c1.setMoral(moral);
                moral = Math.max(c2.getMoral() - 10, 0);
                c2.setMoral(moral);
                logger.info("Les créatures {} et {} ont été démoralisés par {}.", c1.getNomComplet(), c2.getNomComplet(), creatureMourrante.getNomComplet());
        }
    }
}