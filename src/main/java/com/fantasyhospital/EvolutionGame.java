package com.fantasyhospital;

import com.fantasyhospital.controller.GridMedicalServiceController;
import com.fantasyhospital.controller.ListCreatureController;
import com.fantasyhospital.controller.ListDoctorsController;
import com.fantasyhospital.controller.WaitingRoomController;
import com.fantasyhospital.enums.RaceType;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.model.rooms.Room;
import com.fantasyhospital.model.rooms.medicalservice.Crypt;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import com.fantasyhospital.model.rooms.medicalservice.Quarantine;
import com.fantasyhospital.observer.ExitObserver;
import com.fantasyhospital.observer.MoralObserver;
import com.fantasyhospital.util.EndGameSummary;
import com.fantasyhospital.util.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
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
    private static final double GET_NEW_DISEASE_CHANCE = 0.1;
    private static final double EVOLVE_LEVEL_DISEASE_CHANCE = 0.1;
    private static final double EVOLVE_BUDGET_CHANCE = 0.05;
    private static final double ADD_CREATURE_CHANCE = 0.95;
    private static final double ADD_DOCTOR_CHANCE = 0.02;
    private static final double EVOLVE_MORAL_CHANCE = 0.05;
    private static final int VARIATION_MORAL_LEVEL = 30;
	private static final int NB_RANDOM_ADD_CREATURE = 6;

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
		log.info(Singleton.getInstance().getEndGameLog());
	}

	public boolean runNextRound() {
		logRound(round);

		if (executeAndCheckEnd(this::applyDiseasesEffects)) return true;
		if (executeAndCheckEnd(this::doCreaturesWait)) return true;
		if (executeAndCheckEnd(this::doDoctorsExamine)) return true;
		if (executeAndCheckEnd(this::actionCrypte)) return true;
		if (executeAndCheckEnd(this::reviewBudgetServicesClose)) return true;
		if (executeAndCheckEnd(this::reviewHospitalBudget)) return true;

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

		return false;
	}

	public void showEndGame() {
		logEndGame();
	}

	/**
	 * Execute the method given in param and checks if the game is over
	 *
	 * @param action the method to execute
	 * @return boolean true if the game is over, false otherwise
	 */
	private boolean executeAndCheckEnd(Runnable action) {
		action.run();
		endOfGame = checkEndOfGame();
		return endOfGame;
	}

	/**
	 * Do the actions for the crypt
	 */
	private void actionCrypte() {
		Crypt crypt = hospital.getCrypt();
		if(crypt != null) {
			crypt.manageCrypt();
		}
	}

	/**
	 * Check all the medical service's budget, if the budget is at 0 the medical service close
	 */
	private void reviewHospitalBudget(){
		hospital.checkBudgetServices();
		hospital.reviewBudgetServiceCreate();
	}

	private void reviewBudgetServicesClose(){
		hospital.reviewBudgetServicesClose();
	}

	/**
	 * Checks if the hospital is empty (creatures) or if there is only a crypt or quarantine remaining
	 *
	 * @return true if it is, false otherwise.
	 */
	private boolean checkEndOfGame() {
		int totalCreatures = hospital.getTotalCreaturesHospital();
		boolean result = false;
		if(totalCreatures == 0){
			log.info("Partie terminée, il n'y a plus aucune créature dans l'hopital..");
			result = true;
		} else if(hospital.getNbMedicalServicesExceptCryptQuarantine() == 0){
			log.info("Partie terminée, l'hopital ferme, tous les services avaient mis la clé sous la porte faute de budget...");
			result = true;
		}
		return result;
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
				if (!isInQuarantine && Math.random() < GET_NEW_DISEASE_CHANCE) {
					Disease disease = new Disease();
					log.info("La créature {} n'a pas de chance, elle vient de contracter la maladie {} de manière complétement aléatoire.", creature.getFullName(), disease.getName());
					creature.fallSick(disease);
				}

				// A chaque fois que notifyExitObserver est appelé sur la creature, on vérifie si la creature n'a pas été sortie de l'hopital
				// Si c'est le cas, on passe à la creature suivante de la boucle for (pour ne pas effectuer les traitements suivants puisque ça n'aurait pas de sens)
				if (hospital.getRoomOfCreature(creature) == null) {
					continue;
				}

				// Fait monter 1 niveau maladies par tour et perdre 5 moral par maladie (sauf en quarantaine pour le moral)
				for (Disease disease : creature.getDiseases()) {
					if (!isInQuarantine) {
						disease.increaseLevel();
					}
				}

				creature.notifyExitObservers();
				if (hospital.getRoomOfCreature(creature) == null) {
					continue;
				}

				// 10% chance que le niveau d'une de ses maladies évolue de manière aléatoire
				// On vérifie que la créature ne soit pas sortie de l'hôpital avec modifs précédentes
				if (Math.random() < EVOLVE_LEVEL_DISEASE_CHANCE) {
					Disease dis = creature.getRandomDisease();
					dis.setCurrentLevel(Math.min(dis.getCurrentLevel() + new Random().nextInt(8), dis.getLEVEL_MAX() - 1));
					log.warn("La créature {} n'a pas de chance, sa maladie {} passe au niveau {} de manière aléatoire...", creature.getFullName(), dis.getName(), dis.getCurrentLevel());
				}

				creature.notifyExitObservers();

				// 5% chance que le moral evolue de manière random (entre 0 et 30 variation moral)
				if (Math.random() < EVOLVE_MORAL_CHANCE) {
					int variationMoral;
					int newMorale;
					if (new Random().nextBoolean()) {
						variationMoral = -(1 + new Random().nextInt(VARIATION_MORAL_LEVEL + 1));
						newMorale = Math.max(creature.getMorale() + variationMoral, 0);
						log.warn("La créature {} n'a pas de chance car son moral évolue aléatoirement ({})", creature.getFullName(), variationMoral);
					} else {
						variationMoral = 1 + new Random().nextInt(VARIATION_MORAL_LEVEL - 1);
						newMorale = Math.min(creature.getMorale() + variationMoral, 100);
						log.warn("La créature {} a de la chance car son moral évolue aléatoirement (+{})", creature.getFullName(), variationMoral);
					}
					creature.setMorale(newMorale);
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
			}
		}
	}

	/**
	 * Reset the doctors for the new turn
	 */
	private void resetDoctors() {
		for (MedicalService service : hospital.getMedicalServices()) {
			for (Doctor doctor : service.getDoctors()) {
				doctor.resetForNewTurn();
			}
		}
	}


	/**
	 * Executes the actions of the doctors for the hospital.
	 */
	private void doDoctorsExamine() {
		resetDoctors();

		for (MedicalService service : hospital.getMedicalServices()) {
			List<Doctor> doctors = new ArrayList<>(service.getDoctors());
			if (!doctors.isEmpty()) {
				for (Doctor doctor : doctors) {
					doctor.examine(hospital);
				}
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

            int oldBudget = service.getBudget();
            int variation = new Random().nextInt(41) - 20;
			int newBudget = oldBudget + variation;
			newBudget = Math.max(0, Math.min(100, newBudget));
			service.setBudget(newBudget);
            log.warn("Le budget du service {} varie aléatoirement, il passe de {} à {}.", service.getName(), oldBudget, service.getBudget());
        }
    }

	/**
	 * Call the several methods that modify the game randomly each tour
	 */
	private void modifyGameRandomly() {
		addCreatureRandomly();
		addDoctorRandomly();
		reviewRndBudget(hospital);
	}

	/**
	 * Add a random creature to a random service with a random disease with a random level by 95% chance
	 */
	private void addCreatureRandomly() {
		int nbCreatures = 4 + new Random().nextInt(NB_RANDOM_ADD_CREATURE);
		for (int i = 0; i < nbCreatures; i++) {
			if (Math.random() < ADD_CREATURE_CHANCE) {

				// Find a random service that has enough space in it
				Room room = null;
				List<Room> roomsAvailable = new ArrayList<>();
				for(Room roomFor : hospital.getServices()){
					if(roomFor.getAvailableBeds() > 0){
						roomsAvailable.add(roomFor);
					}
				}
				int rnd = new Random().nextInt(roomsAvailable.size());
				room = roomsAvailable.get(rnd);
				if(room == null){
					return;
				}
				Creature creature = null;

				if (room != null) {
					if (Objects.equals(room.getName(), "Crypt")) {
						RaceType race;
						if (room.getCreatures().isEmpty()) {
							race = new Random().nextBoolean() ? RaceType.ZOMBIE : RaceType.VAMPIRE;
							creature = Game.randomCreature(race);

						}
						if (!room.getCreatures().isEmpty()) {
							String racetype = room.getRoomType().toUpperCase();
							race = RaceType.valueOf(racetype);
							creature = Game.randomCreature(race);
						}
					}
					if (Objects.equals(room.getName(), "Quarantaine")) {
						RaceType race;
						if (room.getCreatures().isEmpty()) {
							String randomContaminatingRace = Quarantine.getRandomContaminatingRace();
							race = RaceType.valueOf(randomContaminatingRace.toUpperCase());
							creature = Game.randomCreature(race);
						}
						if (!room.getCreatures().isEmpty()) {
							String racetype = room.getRoomType().toUpperCase();
							race = RaceType.valueOf(racetype);
							creature = Game.randomCreature(race);
						}
					}
					if (!Objects.equals(room.getName(), "Room d'attente") && !Objects.equals(room.getName(), "Quarantaine") && !Objects.equals(room.getName(), "Crypt")) {
						if (!room.getCreatures().isEmpty()) {
							String Racetype = room.getRoomType().toUpperCase();
							RaceType race = RaceType.valueOf(Racetype);
							creature = Game.randomCreature(race);
						}
						if (room.getCreatures().isEmpty()) {
							creature = Game.randomCreature();
						}
					}
					if (room.getName().equals("Room d'attente")) {
						creature = Game.randomCreature();
					}

					creature.setMorale(0);
					creature.getDiseases().get(0).setCurrentLevel(new Random().nextInt(8) + 1);
					creature.addExitObserver(new ExitObserver(hospital));
					creature.addMoralObserver(new MoralObserver(hospital));
					room.addCreature(creature);
					//log.info("Une créature sauvage apparait dans le service {}, bienvenue {} !", room.getName(), creature.getFullName());
				}
			}
		}
	}

	/**
	 * Add a new doctor randomly (2% chance) to a random medical service
	 */
	private void addDoctorRandomly() {
		// Ajout médecin aléatoire
		if (Math.random() < ADD_DOCTOR_CHANCE) {
			MedicalService medicalService = hospital.getMedicalServices().get(new Random().nextInt(hospital.getMedicalServices().size()));

			String race = medicalService.getRoomType();
			Doctor doctor = new Doctor(medicalService);
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

	private void logEndGame() {
		log.info("#############################################");
		log.info("################ FIN DU JEU #################");
		log.info("#############################################");
	}

	public EndGameSummary getEndGameSummary() {
		return Singleton.getInstance().getEndGameSummary();
	}
}
