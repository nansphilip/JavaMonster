package com.fantasyhospital;

import com.fantasyhospital.controller.GridMedicalServiceController;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.fantasyhospital.controller.GridMedicalServiceController;
import com.fantasyhospital.controller.ListCreatureController;
import com.fantasyhospital.controller.ListDoctorsController;
import com.fantasyhospital.controller.WaitingRoomController;
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
import com.fantasyhospital.model.rooms.Room;
import com.fantasyhospital.model.rooms.medicalservice.Crypt;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import com.fantasyhospital.model.rooms.medicalservice.Quarantine;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EvolutionGame {

    /**
     * Game hospital
     */
    private Hospital hospital;
    private int round = 1;
    private boolean endOfGame = false;
    private ListCreatureController listCreatureController;
    private ListDoctorsController listDoctorsController;
    private WaitingRoomController waitingRoomController;
    private GridMedicalServiceController gridMedicalServiceController;

    public EvolutionGame(Hospital hospital, ListCreatureController listCreatureController, ListDoctorsController listDoctorsController, WaitingRoomController waitingRoomController, GridMedicalServiceController gridMedicalServiceController) {
        this.hospital = hospital;
        this.listCreatureController = listCreatureController;
        this.listDoctorsController = listDoctorsController;
        this.waitingRoomController = waitingRoomController;
        this.gridMedicalServiceController = gridMedicalServiceController;
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
        endOfGame = checkEndOfGame();
        applyDiseasesEffects();
        endOfGame = checkEndOfGame();
        doCreaturesWait();
        endOfGame = checkEndOfGame();
        doDoctorsExamine();
        endOfGame = checkEndOfGame();
        actionCrypte();
        endOfGame = checkEndOfGame();
        addRndCreatureRndRoom();

        //hospital.displayServices();

        round++;

        if (listCreatureController != null) {
            listCreatureController.updateCreaturesList();
        }
        if (listDoctorsController != null) {
            listDoctorsController.updateDoctorsList();
        }
        if (waitingRoomController != null) {
            waitingRoomController.updateWaitingRoom();
        }
        if (gridMedicalServiceController != null) {
            gridMedicalServiceController.updateServicesList();
        }
    }


    private void actionCrypte(){
        List<Crypt> cryptList =  hospital.getCrypts();
        if(!cryptList.isEmpty()){
            for(Crypt crypt : cryptList){
                crypt.manageCrypt();
            }
        }
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
                // Si la créature est en quarantaine, on ne lui ajoute pas de nouvelles maladies aléatoirement
                boolean isInQuarantine = room instanceof Quarantine;

                // 5% chance contracter nouvelle maladie (sauf en quarantaine)
                if(!isInQuarantine && Math.random() < 0.05){
                    Disease disease = new Disease();
                    creature.fallSick(disease);
                    log.info("La créature {} n'a pas de chance, elle vient de contracter la maladie {} de manière complétement aléatoire.", creature.getFullName(), disease.getName());
                }

                // Récupère maladie random creature et modifie current level random
                // if(Math.random() < 0.10){
                //    Disease dis = creature.getRandomDisease();
                //    dis.setCurrentLevel(new Random().nextInt(8)+1);
                // }

                // Fait monter 1 niveau maladies par tour et perdre 5 moral par maladie (sauf en quarantaine pour le moral)
                for (Disease disease : creature.getDiseases()) {
                    disease.increaseLevel();

                    // En quarantaine, le moral ne change pas
                    if (!isInQuarantine) {
                        creature.setMorale(Math.max(creature.getMorale() - 5, 0));
                    }
                }
                creature.notifyExitObservers();
            }
        }
    }

    /**
     * Makes all creatures wait with the associated effects.
     * Les créatures en quarantaine n'attendent pas (leur moral est figé)
     */
    private void doCreaturesWait() {
        for (Room room : hospital.getServices()) {
            // Si c'est une quarantaine, les créatures n'attendent pas (moral figé)
            if (room instanceof Quarantine) {
                continue;
            }

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

            if(room != null && room.getCreatures().size() < room.getMAX_CREATURE()){
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

    private void logRound(int round) {
        log.info("#############################################", round);
        log.info("################ TOUR : {} ###################", round);
        log.info("#############################################", round);
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
