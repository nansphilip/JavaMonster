package com.fantasyhospital.model.creatures;

import com.fantasyhospital.model.Hopital;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.salles.Salle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MoralThread implements Runnable {

    private Creature creature;
    private Hopital hopital;

    public MoralThread(Creature creature,  Hopital hopital) {
        this.creature = creature;
        this.hopital = hopital;
    }

    @Override
    public void run() {
        //Checker le moral
//        while(hopital.getSalleOfCreature(creature) != null){
//            //this.creature.verifierMoral(this.hopital.getSalleOfCreature(creature));
//            boolean isDead = this.creature.verifierSante(this.hopital.getSalleOfCreature(creature));
//            log.info("moral thread, creature : {}, boolean : {}", creature.getNomComplet(), isDead);
//            if(isDead){
//                this.hopital.getSalleOfCreature(creature).enleverCreature(this.creature);
//                break;
//            }
//        }
        while(!Thread.currentThread().isInterrupted()){
            for(Salle salle : hopital.getServices()){
                for(Creature creature : salle.getCreatures()){
                    boolean getsOut = creature.hasCreatureToleaveHospital(this.hopital.getSalleOfCreature(creature));
                    String interfaceCreature = "";
                    if(creature.getClass().getInterfaces().length > 0){
                        interfaceCreature = creature.getClass().getInterfaces()[0].getSimpleName();
                    }
                    if(getsOut){
                        this.hopital.getSalleOfCreature(creature).enleverCreature(creature);
                        break;
                    }
                }
            }
        }
    }
}
