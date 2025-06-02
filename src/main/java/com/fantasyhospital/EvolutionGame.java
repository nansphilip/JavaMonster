package com.fantasyhospital;

import com.fantasyhospital.controller.GridMedicalServiceController;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

import com.fantasyhospital.controller.ListCreatureController;
import com.fantasyhospital.controller.ListDoctorsController;
import com.fantasyhospital.controller.WaitingRoomController;
import com.fantasyhospital.enums.BudgetType;
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

    //Constants for the random evolutions
    private static final double GET_NEW_DISEASE_CHANCE = 0.05;
    private static final int DECREASE_DISEASE_MORAL = 5;
    private static final double EVOLVE_LEVEL_DISEASE_CHANCE = 0.10;
    private static final double EVOLVE_BUDGET_CHANCE = 0.3;
    private static final double ADD_CREATURE_CHANCE = 0.95;
    private static final double ADD_DOCTOR_CHANCE = 0.04;
    private static final double EVOLVE_MORAL_CHANCE = 0.95;
    private static final int VARIATION_MORAL_LEVEL = 30;

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
        showCreaturesEndOfGame();
    }

    public boolean runNextRound() {
        logRound(round);

        if (executeAndCheckEnd(this::applyDiseasesEffects)) return true;
        if (executeAndCheckEnd(this::doCreaturesWait)) return true;
        if (executeAndCheckEnd(this::doDoctorsExamine)) return true;
        if (executeAndCheckEnd(this::actionCrypte)) return true;

        modifyGameRandomly();

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
        //hospital.displayServices();
        return false;
    }

    public void showEndGame(){
        logEndGame();
        showCreaturesEndOfGame();
    }

    private boolean executeAndCheckEnd(Runnable action) {
        action.run();
        endOfGame = checkEndOfGame();
        return endOfGame;
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
     * Also make creature sick (5%) and make the level of a random disease of a creature evolve (10%)
     * And make the moral of a creature change (5% chance)
     */
    private void applyDiseasesEffects() {
        for (Room room : hospital.getServices()) {
            for (Creature creature : room.getCreatures()) {
                // Si la créature est en quarantaine, on ne lui ajoute pas de nouvelles maladies aléatoirement
                boolean isInQuarantine = room instanceof Quarantine;

                // 5% chance contracter nouvelle maladie (sauf en quarantaine)
                if(!isInQuarantine && Math.random() < GET_NEW_DISEASE_CHANCE){
                    Disease disease = new Disease();
                    log.info("La créature {} n'a pas de chance, elle vient de contracter la maladie {} de manière complétement aléatoire.", creature.getFullName(), disease.getName());
                    creature.fallSick(disease);
                }

                // A chaque fois que notifyExitObserver est appelé sur la creature, on vérifie si la creature n'a pas été sortie de l'hopital
                // Si c'est le cas, on passe à la creature suivante de la boucle for (pour ne pas effectuer les traitements suivants puisque ça n'aurait pas de sens)
                if(hospital.getRoomOfCreature(creature) == null){
                    continue;
                }

                // Fait monter 1 niveau maladies par tour et perdre 5 moral par maladie (sauf en quarantaine pour le moral)
                for (Disease disease : creature.getDiseases()) {
                    disease.increaseLevel();

                    // En quarantaine, le moral ne change pas
                    if (!isInQuarantine) {
                        creature.setMorale(Math.max(creature.getMorale() - DECREASE_DISEASE_MORAL, 0));
                    }
                }

                creature.notifyExitObservers();
                if(hospital.getRoomOfCreature(creature) == null){
                    continue;
                }

                // 10% chance que le niveau d'une de ses maladies évolue de manière aléatoire
                // On vérifie que la créature ne soit pas sortie de l'hopital avec modifs précédentes
                if(Math.random() < EVOLVE_LEVEL_DISEASE_CHANCE){
                    Disease dis = creature.getRandomDisease();
                    dis.setCurrentLevel(Math.min(dis.getCurrentLevel() + new Random().nextInt(8), dis.getLEVEL_MAX() - 1));
                    log.info("La créature {} n'a pas de chance, sa maladie {} passe au niveau {} de manière aléatoire...", creature.getFullName(), dis.getName(), dis.getCurrentLevel());
                }

                creature.notifyExitObservers();

                // 5% chance que le moral evolue de manière random (entre 0 et 30 variation moral)
                if(Math.random() < EVOLVE_MORAL_CHANCE && !isInQuarantine){
                    int variationMoral;
                    int newMorale;
                    if(new Random().nextBoolean()){
                        variationMoral = - (1 + new Random().nextInt(VARIATION_MORAL_LEVEL + 1));
                        newMorale = Math.max(creature.getMorale() + variationMoral, 0);
                        log.info("La créature {} n'a pas de chance car son moral évolue aléatoirement ({})", creature.getFullName(), variationMoral);
                    } else {
                        variationMoral = 1 + new Random().nextInt(VARIATION_MORAL_LEVEL - 1);
                        newMorale = Math.min(creature.getMorale() + variationMoral, 100);
                        log.info("La créature {} a de la chance car son moral évolue aléatoirement (+{})", creature.getFullName(), variationMoral);
                    }
                    creature.setMorale(newMorale);
                }
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
     * Make a random budget of the hospital change randomly (30% chance)
     */
    private void reviewRndBudget(Hospital hospital) {
        if (Math.random() < EVOLVE_BUDGET_CHANCE && !hospital.getMedicalServices().isEmpty()) {
            int rnd = new Random().nextInt(hospital.getMedicalServices().size());
            MedicalService service = hospital.getMedicalServices().get(rnd);

            BudgetType oldBudget = service.getBudgetType();
            BudgetType newBudget;
            do {
                newBudget = BudgetType.getRandomBudget();
            } while (newBudget == oldBudget);

            service.setBudgetType(newBudget);
            log.info("Budget du service '{}' changé de {} à {}", service.getName(), oldBudget, newBudget);
        }
    }

    /**
     * Call the several methods that modify the game randomly each tour
     */
    private void modifyGameRandomly(){
        addCreatureRandomly();
        addDoctorRandomly();
        reviewRndBudget(hospital);
    }

    /**
     * Add a random creature to a random service with a random disease with a random level by 95% chance
     */
    private void addCreatureRandomly(){
        if(Math.random() < ADD_CREATURE_CHANCE){
            int rnd = new Random().nextInt(hospital.getServices().size());
            Room room = hospital.getServices().get(rnd);
            Creature creature = null;

            if(room != null){
                if (Objects.equals(room.getName(), "Crypt") || Objects.equals(room.getName(), "Zombie")) {
                    RaceType race;
                    if (room.getCreatures().isEmpty()) {
                        race = new Random().nextBoolean() ? RaceType.ZOMBIE : RaceType.VAMPIRE;
                    }
                    else
                    {
                        String Racetype = room.getRoomType().toUpperCase();
                        race = RaceType.valueOf(Racetype);
                    }
                    creature = Game.randomCreature(race);
                }
                else if (!room.getCreatures().isEmpty())
                {
                    String Racetype = room.getRoomType().toUpperCase();
                    RaceType race = RaceType.valueOf(Racetype);
                    creature = Game.randomCreature(race);
                }
                else
                {
                    creature = Game.randomCreature();
                }
                creature.getDiseases().get(0).setCurrentLevel(new Random().nextInt(8)+1);
                creature.addExitObserver(new ExitObserver(hospital));
                creature.addMoralObserver(new MoralObserver(hospital));
                room.addCreature(creature);
                log.info("La créature {} vient d'arriver à l'hosto dans la salle {} ! Bienvenue",  creature.getFullName(), room.getName());
            }
        }
    }

    /**
     * Add a new doctor randomly (4% chance) to a random medical service
     */
    private void addDoctorRandomly(){
        // Ajout médecin aléatoire
        if(Math.random() < ADD_DOCTOR_CHANCE){
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
    public void showCreaturesEndOfGame(){
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
