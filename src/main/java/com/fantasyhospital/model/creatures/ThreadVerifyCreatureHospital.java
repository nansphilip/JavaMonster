package com.fantasyhospital.model.creatures;

import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.rooms.Room;
import lombok.extern.slf4j.Slf4j;

/**
 * Thread responsible for monitoring and managing a creature's morale in the hospital.
 * At each iteration, it checks the creature's morale and health.
 * If the creature dies or leaves the room, it is removed from the hospital.
 */
@Slf4j
public class ThreadVerifyCreatureHospital implements Runnable {

    /**
     * The creature monitored by this thread
     */
    private Creature creature;

    /**
     * The hospital where the creature is located
     */
    private Hospital hospital;

    /**
     * Constructs a morale management thread for a given creature.
     *
     * @param creature the creature to monitor
     * @param hospital the reference hospital
     */
    public ThreadVerifyCreatureHospital(Creature creature, Hospital hospital) {
        this.creature = creature;
        this.hospital = hospital;
    }

    @Override
    public void run() {
        //Checker le moral
//        while(hopital.getRoomOfCreature(creature) != null){
//            //this.creature.verifierMoral(this.hopital.getRoomOfCreature(creature));
//            boolean isDead = this.creature.verifierSante(this.hopital.getRoomOfCreature(creature));
//            log.info("moral thread, creature : {}, boolean : {}", creature.getNomComplet(), isDead);
//            if(isDead){
//                this.hopital.getRoomOfCreature(creature).enleverCreature(this.creature);
//                break;
//            }
//        }
        while(!Thread.currentThread().isInterrupted()){
            for(Room room : hospital.getServices()){
                for(Creature creature : room.getCreatures()){
                    boolean getsOut = creature.hasCreatureToleaveHospital(this.hospital.getRoomOfCreature(creature));
                    String interfaceCreature = "";
                    if(creature.getClass().getInterfaces().length > 0){
                        interfaceCreature = creature.getClass().getInterfaces()[0].getSimpleName();
                    }
                    if(getsOut){
                        this.hospital.getRoomOfCreature(creature).removeCreature(creature);
                        break;
                    }
                }
            }
        }
    }
}
