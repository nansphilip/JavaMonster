package com.fantasyhospital.model.creatures;

import com.fantasyhospital.model.Hopital;
import com.fantasyhospital.model.creatures.abstractclass.Bete;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.salles.Salle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoralThread implements Runnable {

    private Creature creature;
    private Hopital hopital;
    private static final Logger logger = LoggerFactory.getLogger(MoralThread.class);

    public MoralThread(Creature creature,  Hopital hopital) {
        this.creature = creature;
        this.hopital = hopital;
    }

    @Override
    public void run() {
        //Checker le moral
        while(hopital.getSalleOfCreature(creature) != null){
            this.creature.verifierMoral(this.hopital.getSalleOfCreature(creature));
            if(this.creature.verifierSante(this.hopital.getSalleOfCreature(creature))){
                this.hopital.getSalleOfCreature(creature).enleverCreature(this.creature);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                this.hopital.afficherToutesCreatures();
                break;
            }
        }
    }
}
