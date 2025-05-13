package com.fantasyhospital;

import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.interfaces.Regenerating;
import com.fantasyhospital.model.creatures.races.Vampire;
import com.fantasyhospital.model.creatures.races.Zombie;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.rooms.Room;
import com.fantasyhospital.rooms.medicalservice.MedicalService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Scanner;

@Slf4j
public class EvolutionGameThread implements Runnable {

    /**
     * Game hospital
     */
    private Hospital hospital;

    public EvolutionGameThread(Hospital hospital) {
        this.hospital = hospital;
    }

    @Override
    public void run() {
        boolean endOfGame = false;
        int round = 1;
        Scanner sc  = new Scanner(System.in);

        while (!endOfGame) {
            logRound(round);
            sc.nextLine();

            if (checkEndOfGame()) break;
            applyDiseasesEffects();
            if (checkEndOfGame()) break;
            doCreaturesWait();
            if (checkEndOfGame()) break;
            verifyMoraleCreatures();
            if (checkEndOfGame()) break;
            doDoctorsExamine();

            hospital.displayServices();
            round++;
        }
        sc.close();
        logEndGame();
    }

    // Méthodes privées extraites

    private void logRound(int round) {
        log.info("#############################################", round);
        log.info("################ TOUR : {} ###################", round);
        log.info("#############################################", round);
    }

    /**
     * Checks if the hospital is empty.
     * @return true if it is, false otherwise.
     */
    private boolean checkEndOfGame() {
        int TotalCreatures = hospital.getTotalCreaturesHospital();
        if (TotalCreatures == 0) {
            return true;
        }
        return false;
    }

    /**
     * Applies the effects and evolutions of diseases to all creatures.
     */
    private void applyDiseasesEffects() {
        for (Room room : hospital.getServices()) {
            for (Creature creature : room.getCreatures()) {
                for (Disease disease : creature.getDiseases()) {
                    disease.increaseLevel();
                    verifyCreatureOutHospital(creature);
                    creature.setMorale(Math.max(creature.getMorale() - 5, 0));
                }
            }
        }
    }

    /**
     * Makes all creatures wait with the associated effects.
     */
    private void doCreaturesWait() {
        for (Room room : hospital.getServices()) {
            for (Creature creature : room.getCreatures()) {
                creature.waiting(hospital.getRoomOfCreature(creature));
                verifyCreatureOutHospital(creature);
            }
        }
    }

    /**
     * Checks the morale of each creature and the associated effects.
     */
    private void verifyMoraleCreatures() {
        for (Room room : hospital.getServices()) {
            for (Creature creature : room.getCreatures()) {
                creature.checkMorale(hospital.getRoomOfCreature(creature));
                verifyCreatureOutHospital(creature);
            }
        }
    }

    /**
     * Executes the actions of the doctors for the hospital.
     */
    private void doDoctorsExamine() {
        for (MedicalService service : hospital.getMedicalServices()) {
            List<Doctor> medecins = service.getDoctors();
            for (Doctor medecin : medecins) {
                Creature creature = medecin.examine(hospital);
                verifyCreatureOutHospital(creature);
            }
        }
    }

    private void logEndGame() {
        log.info("#############################################");
        log.info("################ FIN DU JEU #################");
        log.info("#############################################");
    }

    /**
     * Function that checks if the creature should leave the hospital (death or healed).
     * @param creature
     */
    public void verifyCreatureOutHospital(Creature creature){
        Room roomCreature = this.hospital.getRoomOfCreature(creature);
        //Si la créature est déjà sortie de l'hopital
        if(roomCreature == null){
            return;
        }

        //Récupération interface creature pour les regenerants
        String interfaceCreature = "";
        if(creature.getClass().getInterfaces().length > 0){
            interfaceCreature = creature.getClass().getInterfaces()[0].getSimpleName();
        }

        //Avant de potentiellement faire trepasser la creature, si regenerant, on check si creature va mourir
        //Si va mourir, on appliquera depression sur medecin
        boolean isDead = false;
        if(Regenerating.class.isAssignableFrom(creature.getClass())){
            if(creature instanceof Zombie zombie){
               isDead = zombie.isCreatureDeadButWillRevive(creature);
            } else if (creature instanceof Vampire vampire){
                isDead = vampire.isCreatureDeadButWillRevive(creature);
            } else {
                throw new RuntimeException("Unrecognized creature class: " + creature.getClass().getName());
            }
        }

        boolean getsOut = creature.hasCreatureToleaveHospital(roomCreature);

        //Si creature meurt, médecins du service perd du moral
        if(getsOut){
            log.info("creature {} sort, nb diseases : {}", creature.getFullName(), creature.getDiseases().size());
            if(!creature.getDiseases().isEmpty()){
                if(roomCreature instanceof MedicalService){
                    List<Doctor> doctors = ((MedicalService) roomCreature).getDoctors();
                    for(Doctor doctor : doctors){
                        doctor.depression();
                    }
                }
            }
            roomCreature.removeCreature(creature);
            return;
        }

        //Si regenerant qui meurt mais reste quand même dans l'hopital, applique depression a medecin
        if(isDead){
            if(roomCreature instanceof MedicalService){
                List<Doctor> doctors = ((MedicalService) roomCreature).getDoctors();
                for(Doctor doctor : doctors){
                    doctor.depression();
                }
            }
        }
    }
}
