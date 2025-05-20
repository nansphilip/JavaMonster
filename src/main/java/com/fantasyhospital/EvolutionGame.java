package com.fantasyhospital;

import com.fantasyhospital.enums.GenderType;
import com.fantasyhospital.enums.RaceType;
import com.fantasyhospital.enums.StackType;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.BeastUtils;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.observer.ExitObserver;
import com.fantasyhospital.observer.MoralObserver;
import com.fantasyhospital.rooms.Room;
import com.fantasyhospital.rooms.medicalservice.MedicalService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

@Slf4j
public class EvolutionGame {

    /**
     * Game hospital
     */
    private Hospital hospital;
    private int round = 1;
    private boolean endOfGame = false;

    public EvolutionGame(Hospital hospital) {
        this.hospital = hospital;
    }

    public void run() {
        Scanner sc = new Scanner(System.in);

        while (!endOfGame) {
            log.info("Appuie sur Entrée pour lancer le prochain tour...");
            sc.nextLine();
            runNextRound();
        }

        sc.close();
        logEndGame();
        afficherCreaturesSortiesHospital();
    }

    public void runNextRound() {
        if (endOfGame) return;

        logRound(round);

        if (checkEndOfGame()) return;
        applyDiseasesEffects();
        if (checkEndOfGame()) return;
        doCreaturesWait();
        if (checkEndOfGame()) return;
        doDoctorsExamine();
        if (checkEndOfGame()) return;
        addRndCreatureRndRoom();

        hospital.displayServices();

        round++;
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
        return TotalCreatures == 0;
    }

    /**
     * Applies the effects and evolutions of diseases to all creatures.
     * Also make creature sick (5%) and make evolve the diseases randomly
     */
    private void applyDiseasesEffects() {
        for (Room room : hospital.getServices()) {
            for (Creature creature : room.getCreatures()) {
                //5% chance contracter nouvelle maladie
                if(Math.random() < 0.05){
                    Disease disease = new Disease();
                    creature.fallSick(disease);
                    log.info("La créature {} n'a pas de chance, elle vient de contracter la maladie {} de manière complétement aléatoire.", creature.getFullName(), disease.getName());
                }

                //Récupère maladie random creature et modifie current level random
//                if(Math.random() < 0.10){
//                    Disease dis = creature.getRandomDisease();
//                    dis.setCurrentLevel(new Random().nextInt(8)+1);
//                }

                //fait monter 1 niveau maladies par tour et perdre 5 moral par maladie
                for (Disease disease : creature.getDiseases()) {
                    disease.increaseLevel();
                    creature.setMorale(Math.max(creature.getMorale() - 5, 0));
                }
                creature.notifyExitObservers();
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
                medecin.examine(hospital);
            }
        }
    }

    /**
     * Ajoute une nouvelle créature avec une maladie, un niveau aléatoire dans une room aléatoire chance 50%
     * Ajoute un nouveau médecin 4% chance
     */
    private void addRndCreatureRndRoom(){
        if(Math.random() < 0.50){
            int rnd = new Random().nextInt(hospital.getServices().size());
            Room room = hospital.getServices().get(rnd);

            if(room != null){
                Creature creature = null;
                if(room.getCreatures().isEmpty()){
                    creature = Game.randomCreature();
                } else {
                    String type = room.getRoomType().toUpperCase();
                    RaceType race = RaceType.valueOf(type);
                    creature = Game.randomCreature(race);
                }
                creature.getDiseases().get(0).setCurrentLevel(new Random().nextInt(8)+1);
                creature.addExitObserver(new ExitObserver(hospital));
                creature.addMoralObserver(new MoralObserver(hospital));
                room.addCreature(creature);
                log.info("La créature {} vient d'arriver à l'hosto dans la salle {} ! Bienvenue",  creature.getFullName(), room.getName());
            }
        }

        if(Math.random() < 0.04){
            MedicalService medicalService = hospital.getMedicalServices().get(new Random().nextInt(hospital.getMedicalServices().size()));
            Doctor doctor = new Doctor(BeastUtils.generateRandomName(GenderType.FEMALE), GenderType.FEMALE, 70, 175, 45, 100, "Lycanthrope", medicalService);
            doctor.addObserver(new MoralObserver(hospital));
            medicalService.addDoctor(doctor);
            log.info("Le médecin {} vient d'arriver dans le service {} !", doctor.getFullName(), medicalService.getName());
        }
    }


    /**
     * Récupère toutes les créatures soignées et trépassées des stack du singleton
     * et les affiche
     */
    public void afficherCreaturesSortiesHospital(){
        Singleton instance = Singleton.getInstance();

        log.info("#################################");
        log.info("#### CREATURES TREPASSEES : #####");
        log.info("#################################");

        while(!instance.isStackEmpty(StackType.DIE)){
            log.info("{}", instance.popBeastFromStack(StackType.DIE));
        }

        log.info("#################################");
        log.info("###### CREATURES SOIGNEES : #####");
        log.info("#################################");

        while(!instance.isStackEmpty(StackType.HEAL)){
            log.info("{}", instance.popBeastFromStack(StackType.HEAL));
        }

        log.info("#################################");
        log.info("###### MEDECINS HARAKIRI : ######");
        log.info("#################################");

        while(!instance.isStackEmpty(StackType.DOCTOR)){
            log.info("{}", instance.popBeastFromStack(StackType.DOCTOR));
        }
    }

    private void logEndGame() {
        log.info("#############################################");
        log.info("################ FIN DU JEU #################");
        log.info("#############################################");
    }
}
