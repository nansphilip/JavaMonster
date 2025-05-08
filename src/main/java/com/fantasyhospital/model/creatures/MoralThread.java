package com.fantasyhospital.model.creatures;

import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.salles.Salle;
import lombok.extern.slf4j.Slf4j;

/**
 * Thread chargé de surveiller et de gérer le moral d'une créature dans l'hôpital.
 *
 * À chaque itération, il vérifie le moral et la santé de la créature.
 * Si la créature décède ou quitte la salle, elle est retirée de l'hôpital.
 */
@Slf4j
public class MoralThread implements Runnable {

    /**
     * La créature surveillée par ce thread
     */
    private Creature creature;

    /**
     * L'hôpital dans lequel se trouve la créature
     */
    private Hospital hospital;

    /**
     * Construit un thread de gestion du moral pour une créature donnée.
     *
     * @param creature la créature à surveiller
     * @param hospital l'hôpital de référence
     */
    public MoralThread(Creature creature, Hospital hospital) {
        this.creature = creature;
        this.hospital = hospital;
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
            for(Salle salle : hospital.getServices()){
                for(Creature creature : salle.getCreatures()){
                    boolean getsOut = creature.hasCreatureToleaveHospital(this.hospital.getSalleOfCreature(creature));
                    String interfaceCreature = "";
                    if(creature.getClass().getInterfaces().length > 0){
                        interfaceCreature = creature.getClass().getInterfaces()[0].getSimpleName();
                    }
                    if(getsOut){
                        this.hospital.getSalleOfCreature(creature).enleverCreature(creature);
                        break;
                    }
                }
            }
        }
    }
}
