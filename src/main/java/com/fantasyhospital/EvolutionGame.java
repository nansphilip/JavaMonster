package com.fantasyhospital;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

import org.springframework.stereotype.Service;

import com.fantasyhospital.controller.GridMedicalServiceController;
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
import com.fantasyhospital.model.rooms.Room;
import com.fantasyhospital.model.rooms.medicalservice.Crypt;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import com.fantasyhospital.model.rooms.medicalservice.Quarantine;
import com.fantasyhospital.observer.ExitObserver;
import com.fantasyhospital.observer.MoralObserver;

import lombok.extern.slf4j.Slf4j;

/**
 * Main simulation engine for Fantasy Hospital. Manages the temporal evolution
 * of the simulation through game rounds.
 */
@Slf4j
@Service
public class EvolutionGame {

	/**
	 * The hospital being simulated.
	 */
	final private Hospital hospital;

	/**
	 * Current round number.
	 */
	private int round = 1;

	/**
	 * Game end flag.
	 */
	private boolean endOfGame = false;

	/**
	 * JavaFX controller for creature list.
	 */
	final private ListCreatureController listCreatureController;

	/**
	 * JavaFX controller for doctors list.
	 */
	final private ListDoctorsController listDoctorsController;

	/**
	 * JavaFX controller for waiting room.
	 */
	final private WaitingRoomController waitingRoomController;

	/**
	 * JavaFX controller for medical services grid.
	 */
	final private GridMedicalServiceController gridMedicalServiceController;

	//Constants for the random evolutions
	private static final double GET_NEW_DISEASE_CHANCE = 0.05;
	private static final int DECREASE_DISEASE_MORAL = 5;
	private static final double EVOLVE_LEVEL_DISEASE_CHANCE = 0.10;
	private static final double EVOLVE_BUDGET_CHANCE = 0.3;
	private static final double ADD_CREATURE_CHANCE = 0.95;
	private static final double ADD_DOCTOR_CHANCE = 0.04;
	private static final double EVOLVE_MORAL_CHANCE = 0.95;
	private static final int VARIATION_MORAL_LEVEL = 30;

	/**
	 * Creates a new evolution game.
	 *
	 * @param hospital the hospital to simulate
	 * @param listCreatureController creature list controller (can be null)
	 * @param listDoctorsController doctors list controller (can be null)
	 * @param waitingRoomController waiting room controller (can be null)
	 * @param gridMedicalServiceController medical services controller (can be
	 * null)
	 */
	public EvolutionGame(Hospital hospital, ListCreatureController listCreatureController, ListDoctorsController listDoctorsController, WaitingRoomController waitingRoomController, GridMedicalServiceController gridMedicalServiceController) {
		this.hospital = hospital;
		this.listCreatureController = listCreatureController;
		this.listDoctorsController = listDoctorsController;
		this.waitingRoomController = waitingRoomController;
		this.gridMedicalServiceController = gridMedicalServiceController;
	}

	/**
	 * Runs the simulation in interactive console mode.
	 */
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

	/**
	 * Executes the next round of simulation.
	 *
	 * @return true if game ended, false otherwise
	 */
	public boolean runNextRound() {
		logRound(round);

		if (executeAndCheckEnd(this::applyDiseasesEffects)) {
			return true;
		}
		if (executeAndCheckEnd(this::doCreaturesWait)) {
			return true;
		}
		// if (executeAndCheckEnd(this::doDoctorsExamine)) {
		//     return true;
		// }
		if (executeAndCheckEnd(this::actionCrypte)) {
			return true;
		}

		// modifyGameRandomly();
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

	/**
	 * Shows end game statistics.
	 */
	public void showEndGame() {
		logEndGame();
		log.info(Singleton.getInstance().getEndGameLog());
	}

	/**
	 * Executes an action and checks if game should end.
	 *
	 * @param action the action to execute
	 * @return true if game ended, false otherwise
	 */
	private boolean executeAndCheckEnd(Runnable action) {
		action.run();
		endOfGame = checkEndOfGame();
		return endOfGame;
	}

	/**
	 * Manages crypt actions.
	 */
	private void actionCrypte() {
		List<Crypt> cryptList = hospital.getCrypts();
		if (!cryptList.isEmpty()) {
			for (Crypt crypt : cryptList) {
				crypt.manageCrypt();
			}
		}
	}

	/**
	 * Checks if the hospital is empty.
	 *
	 * @return true if hospital is empty, false otherwise
	 */
	private boolean checkEndOfGame() {
		int totalCreatures = hospital.getTotalCreaturesHospital();
		return totalCreatures == 0;
	}

	/**
	 * Applies disease effects and evolutions to all creatures. Makes creatures
	 * sick (5% chance) and evolves diseases.
	 */
	private void applyDiseasesEffects() {
		for (Room room : hospital.getServices()) {
			for (Creature creature : room.getCreatures()) {
				// If creature is in quarantine, don't add new random diseases
				boolean isInQuarantine = room instanceof Quarantine;

				// 5% chance to contract new disease (except in quarantine)
				if (!isInQuarantine && Math.random() < GET_NEW_DISEASE_CHANCE) {
					Disease disease = new Disease();
					log.info("La créature {} n'a pas de chance, elle vient de contracter la maladie {} de manière complétement aléatoire.", creature.getFullName(), disease.getName());
					creature.fallSick(disease);
				}

				// Increase disease level by 1 per turn and lose 5 morale per disease (except in quarantine for morale)
				for (Disease disease : creature.getDiseases()) {
					disease.increaseLevel();

					// In quarantine, morale doesn't change
					if (!isInQuarantine) {
						creature.setMorale(Math.max(creature.getMorale() - DECREASE_DISEASE_MORAL, 0));
					}
				}

				creature.notifyExitObservers();
				if (hospital.getRoomOfCreature(creature) == null) {
					continue;
				}

				// 10% chance que le niveau d'une de ses maladies évolue de manière aléatoire
				// On vérifie que la créature ne soit pas sortie de l'hopital avec modifs précédentes
				if (Math.random() < EVOLVE_LEVEL_DISEASE_CHANCE) {
					Disease dis = creature.getRandomDisease();
					dis.setCurrentLevel(Math.min(dis.getCurrentLevel() + new Random().nextInt(8), dis.getLEVEL_MAX() - 1));
					log.info("La créature {} n'a pas de chance, sa maladie {} passe au niveau {} de manière aléatoire...", creature.getFullName(), dis.getName(), dis.getCurrentLevel());
				}

				creature.notifyExitObservers();

				// 5% chance que le moral evolue de manière random (entre 0 et 30 variation moral)
				if (Math.random() < EVOLVE_MORAL_CHANCE && !isInQuarantine) {
					int variationMoral;
					int newMorale;
					if (new Random().nextBoolean()) {
						variationMoral = -(1 + new Random().nextInt(VARIATION_MORAL_LEVEL + 1));
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
	 * Makes all creatures wait with associated effects. Creatures in quarantine
	 * don't wait (morale is frozen).
	 */
	private void doCreaturesWait() {
		for (Room room : hospital.getServices()) {
			// If it's quarantine, creatures don't wait (morale frozen)
			if (room instanceof Quarantine) {
				continue;
			}

			for (Creature creature : room.getCreatures()) {
				creature.waiting(hospital.getRoomOfCreature(creature));
			}
		}
	}

	/**
	 * Executes doctors' actions for the hospital.
	 */
//    private void doDoctorsExamine() {
//        for (MedicalService service : hospital.getMedicalServices()) {
//            List<Doctor> medecins = service.getDoctors();
//            for (Doctor medecin : medecins) {
//                medecin.examine(hospital);
//            }
//        }
//    }
	/**
	 * Randomly modifies a service's budget (10% chance).
	 *
	 * @param hospital the hospital to modify
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
	 * Adds a new creature with random disease level to a random room (95%
	 * chance). Adds a new doctor (4% chance).
	 */
	private void addCreatureRandomly() {
		if (Math.random() < ADD_CREATURE_CHANCE) {
			int rnd = new Random().nextInt(hospital.getServices().size());
			Room room = hospital.getServices().get(rnd);
			Creature creature = null;

			if (room != null) {
				if (Objects.equals(room.getName(), "Crypt") || Objects.equals(room.getName(), "Zombie")) {
					RaceType race;
					if (room.getCreatures().isEmpty()) {
						race = new Random().nextBoolean() ? RaceType.ZOMBIE : RaceType.VAMPIRE;
					} else {
						String Racetype = room.getRoomType().toUpperCase();
						race = RaceType.valueOf(Racetype);
					}
					creature = Game.randomCreature(race);
				} else if (!room.getCreatures().isEmpty()) {
					String raceType = room.getRoomType().toUpperCase();
					RaceType race = RaceType.valueOf(raceType);
					creature = Game.randomCreature(race);
				} else {
					creature = Game.randomCreature();
				}
				creature.getDiseases().get(0).setCurrentLevel(new Random().nextInt(8) + 1);
				creature.addExitObserver(new ExitObserver(hospital));
				creature.addMoralObserver(new MoralObserver(hospital));
				room.addCreature(creature);
				log.info("La créature {} vient d'arriver à l'hosto dans la salle {} ! Bienvenue", creature.getFullName(), room.getName());
			}
		}
	}

	/**
	 * Add a new doctor randomly (4% chance) to a random medical service
	 */
	private void addDoctorRandomly() {
		// Ajout médecin aléatoire
		if (Math.random() < ADD_DOCTOR_CHANCE) {
			MedicalService medicalService = hospital.getMedicalServices().get(new Random().nextInt(hospital.getMedicalServices().size()));
			Doctor doctor = new Doctor(BeastUtils.generateRandomName(GenderType.FEMALE), GenderType.FEMALE, 70, 175, 45, 100, "Lycanthrope", medicalService);
			doctor.addObserver(new MoralObserver(hospital));
			medicalService.addDoctor(doctor);
			log.info("Le médecin {} vient d'arriver dans le service {} !", doctor.getFullName(), medicalService.getName());
		}
	}

	/**
	 * Logs the current round header.
	 *
	 * @param round the round number
	 */
	private void logRound(int round) {
		log.info("#############################################", round);
		log.info("################ TOUR : {} ###################", round);
		log.info("#############################################", round);
	}

	/**
	 * Retrieves and displays all healed and deceased creatures from singleton
	 * stacks.
	 */
	public void showCreaturesEndOfGame() {
		Singleton instance = Singleton.getInstance();

		log.info("#################################");
		log.info("#### CREATURES TREPASSEES : #####");
		log.info("#################################");

		while (!instance.isStackEmpty(StackType.DIE)) {
			log.info("{}", instance.popBeastFromStack(StackType.DIE));
		}

		log.info("#################################");
		log.info("###### CREATURES SOIGNEES : #####");
		log.info("#################################");

		while (!instance.isStackEmpty(StackType.HEAL)) {
			log.info("{}", instance.popBeastFromStack(StackType.HEAL));
		}

		log.info("#################################");
		log.info("###### MEDECINS HARAKIRI : ######");
		log.info("#################################");

		while (!instance.isStackEmpty(StackType.DOCTOR)) {
			log.info("{}", instance.popBeastFromStack(StackType.DOCTOR));
		}
	}

	/**
	 * Logs the end game message.
	 */
	private void logEndGame() {
		log.info("#############################################");
		log.info("################ FIN DU JEU #################");
		log.info("#############################################");
	}

	public String getEndGameSummary() {
		return Singleton.getInstance().getEndGameLog();
	}
}
