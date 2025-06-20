package com.fantasyhospital.model.creatures;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fantasyhospital.util.Singleton;
import com.fantasyhospital.enums.ActionType;
import com.fantasyhospital.enums.GenderType;
import com.fantasyhospital.enums.StackType;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.abstractclass.Beast;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.observer.CreatureObserver;
import com.fantasyhospital.model.rooms.Room;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import static com.fantasyhospital.enums.RaceType.generateRandomRace;
import static com.fantasyhospital.model.creatures.abstractclass.BeastUtils.*;
import static com.fantasyhospital.model.creatures.abstractclass.BeastUtils.generateAge;
import static com.fantasyhospital.model.creatures.abstractclass.BeastUtils.generateMorale;
import static com.fantasyhospital.model.creatures.abstractclass.BeastUtils.generateRandomName;

/**
 * Represents a doctor in Fantasy Hospital. A doctor can heal,
 * transfer creatures, review the budget, and manage their own morale.
 */

@Setter
@Getter
@Slf4j
public class Doctor extends Beast {

	/**
	 * Race/type of the doctor (e.g., Lycanthrope)
	 */
	protected String race;

	/**
	 * Boolean indicating if the doctor has harakiri yet
	 */
	private boolean harakiriTriggered = false;

	/**
	 * Medical service to which the doctor is assigned
	 */
	protected MedicalService medicalService;

	/**
	 * List of observers that monitor the doctor's state
	 */
	private List<CreatureObserver> observers = new ArrayList<>();

	/**
	 * Boolean indicating if the doctor has moved this turn.
	 */
	private boolean hasMovedThisTurn = false;

	// Constants of modification
	public static final int INCREASE_BUDGET_SERVICE = 2;

	/**
	 * Default Constructor that creates a doctor with their characteristics and assigned service.
	 * @param name
	 * @param sex
	 * @param weight
	 * @param height
	 * @param age
	 * @param morale
	 * @param race
	 * @param medicalService
	 */
	public Doctor(String name, GenderType sex, int weight, int height, int age, int morale, String race, MedicalService medicalService) {
		super(name, sex, weight, height, age, morale);
		this.race = race;
		this.medicalService = medicalService;
		this.previousMorale = morale;
	}

	/**
	 * Constructor that generates a random doctor in a medical service.
	 * @param medicalService the medical service to which the doctor is assigned
	 */
	public Doctor(MedicalService medicalService) {
		super(generateRandomRace(), generateRandomSex(), generateWeight(), generateHeight(), generateAge(), generateMorale());
		this.fullName = "Dr. " + generateRandomName(this.sex);
		this.race = race;
		this.medicalService = medicalService;
	}

	/**
	 * Doctor's waiting action (to be completed if needed).
	 */
	@Override
	public void waiting(Room room) {

	}

	/**
	 * method of dying
	 *
	 * @param room the room where the beast is dying
	 * @return true if the creature gets out of the hospital, false otherwise (in the case of regenerating interface)
	 */
	@Override
	public boolean die(Room room) {
		return false;
	}

	/**
	 * Add the observer to the list of observers that monitor the doctor's state.
	 *
	 * @param creatureObserver
	 */
	public void addObserver(CreatureObserver creatureObserver) {
		observers.add(creatureObserver);
	}

	/**
	 * Notify all observers that the doctor's state has changed.
	 */
	private void notifyObservers() {
		for (CreatureObserver observer : observers) {
			observer.onStateChanged(this);
		}
	}

	@Override
	public void setMorale(int morale) {
		this.previousMorale = this.morale;
		this.morale = morale;
		notifyObservers();
	}

	/**
	 * Examines a medical service.
	 * If the doctor's assigned service is empty, they transfer a creature from the waiting room or another service.
	 * The doctor heals the sickest creature (based on highest disease level or number of diseases).
	 * V2 Evolution:
	 * The doctor prioritizes healing contaminating and demoralizing creatures, and avoids regenerating ones.
	 * The doctor also considers the morale of creatures.
	 * Instead of always fetching a creature from the waiting room, they check other services
	 * to transfer a creature that requires more urgent care.
	 * They may isolate a creature that is about to die.
	 * Other potential actions...
	 */
	public void examine(Hospital hospital) {
		// Check if creatures exists
		if (hospital.getTotalCreaturesHospital() == 0) {
			return;
		}
		if (hasMovedThisTurn) {
			hasMovedThisTurn = false;
			return;
		}

		// Get creatures list
		CopyOnWriteArrayList<Creature> listeCreatures = this.medicalService.getCreatures();

		// Si il est dans un service avec au moins un autre médecin, il se transfère dans un service sans médecin/avec le plus
		// Grand nombre de créatures
		if(this.medicalService.getDoctors().size() >= 2){
			MedicalService medicalServiceWithNoDoctor = hospital.getMedicalServiceWithNoDoctor();
			if(medicalServiceWithNoDoctor != null) {
				log.info("Le médecin {} se transfère de {} vers {} car il etait dans un service avec d'autres médecins", this.fullName, this.medicalService.getName(), medicalServiceWithNoDoctor.getName());
				goTo(this.medicalService, medicalServiceWithNoDoctor);
				return;
			}

			MedicalService medicalServiceWithNbMaxCreatures = hospital.getMedicalServiceWithNbMaxCreatures();
			if(medicalServiceWithNbMaxCreatures != null) {
				log.info("Le médecin {} se transfere de {} vers {} car le service de destination a plein de creatures",  this.fullName, this.medicalService.getName(), medicalServiceWithNbMaxCreatures.getName());
				goTo(this.medicalService, medicalServiceWithNbMaxCreatures);
				return;
			}
		}

		// Si son service médical est vide, il essaie de transférer une créature de la room d'attente vers son service.
		// Il doit chercher un service du type de la créature, sinon choisir une autre créature.
		if (listeCreatures.isEmpty()) {
			Room waitingRoom = hospital.getWaitingRoom();
			if (waitingRoom != null) {
				//Il reste des creatures dans la salle d'attente à soigner, il essaie d'en transférer le plus grand nombre de la même race.
				if (!waitingRoom.getCreatures().isEmpty()) {
					List<Creature> creaturesToTransfer = waitingRoom.getAllCreaturesOfSameRace();
					if (creaturesToTransfer != null) {
						if (authorizedToGoToCrypt(creaturesToTransfer)) {
							if (transferToSpecialService(hospital.getCrypt(), creaturesToTransfer, waitingRoom)) {
								return;
							}
							if (!this.medicalService.getName().equals("Quarantaine")) {
								transferGroup(creaturesToTransfer, waitingRoom, this.medicalService, false);
								return;
							}
						}
						if (authorizedToGoToQuarantine(creaturesToTransfer)) {
							if (transferToSpecialService(hospital.getQuarantine(), creaturesToTransfer, waitingRoom)) {
								MedicalService quarantine = hospital.getMedicalServiceByName("Quarantaine");
								goTo(this.medicalService, quarantine);
								return;
							} else if (!this.medicalService.getName().equals("Crypt")) {
								transferGroup(creaturesToTransfer, waitingRoom, this.medicalService, false);
								return;
							}
						}
						if (!authorizedToGoToCrypt(creaturesToTransfer) && !authorizedToGoToQuarantine(creaturesToTransfer)) {
							transferGroup(creaturesToTransfer, waitingRoom, this.medicalService, false);
							return;
						}
					}
				}
				if (waitingRoom.getCreatures().isEmpty()) {
					//Si la salle d'attente est vide, le médecin va regarder dans les autres services s'il reste des créatures à soigner et va s'y rendre.
					//S'il ne reste qu'une créature dans l'hôpital (donc dans un service) et qu'il y a un médecin dans le service, il laisse l'autre médecin la soigner.
					for (MedicalService service : hospital.getMedicalServices()) {
						Creature creatureToHeal = service.getCreatureWithHighLevelDisease();
						if (creatureToHeal != null && !service.getName().equals("Crypt")) {
							if (service.getDoctors().isEmpty()) {
								goTo(this.medicalService, service);
								return;
							}
							if (hospital.getTotalCreaturesHospital() == 1 && !service.getDoctors().isEmpty()) {
								doctorWait();
								return;
							}
							if (service.getCreatures().size() > service.getDoctors().size()) {
								goTo(this.medicalService, service);
								return;
							}
						}
					}
				}
			}
		}

		// Récupération de la créature avec le niveau de disease le plus avancé et le nombre de diseases le plus élevé
		Creature creatureMaxLvlDisease = this.medicalService.getCreatureWithHighLevelDisease();
		Creature creatureMaxDiseases = this.medicalService.getCreatureWithNbMaxDisease();

		if (creatureMaxDiseases == null || creatureMaxLvlDisease == null) {
			doctorWait();
			return;
		}

		//Soigne créature si niveau disease >= 8, sinon si nombre maladies >= 3
		if (creatureMaxLvlDisease.getHighLevelDisease().getCurrentLevel() >= 8) {
			heal(creatureMaxLvlDisease, creatureMaxLvlDisease.getHighLevelDisease());
			return;
		} else if (creatureMaxDiseases.getDiseases().size() >= 3) {
			heal(creatureMaxDiseases, creatureMaxDiseases.getHighLevelDisease());
			return;
		}

		heal(creatureMaxLvlDisease, creatureMaxLvlDisease.getHighLevelDisease());

	}

	/**
	 * Checks if the list of creatures is authorized to go to the Crypt.
	 * @param creatures
	 * @return true if it is authorized, false otherwise
	 */
	private boolean authorizedToGoToCrypt(List<Creature> creatures) {
		return creatures.getFirst().getRace().equals("Zombie") || creatures.getFirst().getRace().equals("Vampire");
	}

	/**
	 * Checks if the list of creatures is authorized to go to the Quarantine.
	 * @param creatures
	 * @return true if it is authorized, false otherwise
	 */
	private boolean authorizedToGoToQuarantine(List<Creature> creatures) {
		return creatures.getFirst().getRace().equals("Orc") || creatures.getFirst().getRace().equals("Werebeast") || creatures.getFirst().getRace().equals("Lycanthrope") || creatures.getFirst().getRace().equals("Vampire");
	}

	/**
	 * Move the doctor from one medical service to another.
	 *
	 * @param roomFrom the medical service from which the doctor is leaving.
	 * @param roomTo the medical service to which the doctor is going.
	 */
	public void goTo(MedicalService roomFrom, MedicalService roomTo) {
		roomFrom.removeDoctor(this);
		roomTo.addDoctor(this);
		this.medicalService = roomTo;
		this.hasMovedThisTurn = true;
		log.info("Le docteur {} part du service {} et va dans le service {}", this.fullName, roomFrom.getName(), roomTo.getName());
	}

	/**
	 * Makes the doctor wait when there are no actions to perform.
	 */
	private void doctorWait() {
		log.info("Le docteur {} n'a rien à faire...", this.fullName);
	}

	/**
	 * Heals the disease of a creature with the highest level.
	 *
	 * @param creature the creature to heal
	 * @param disease  the disease to heal
	 */
	private void heal(Creature creature, Disease disease) {
		if (disease == null) {
			log.error("[medecin][soigner()] La créature {} n'a pas de disease", creature.getFullName());
			return;
		}

		if (!creature.beCured(disease, this.medicalService)) {
			log.error("[medecin][soigner()] La créature {} ne possédait pas la disease {}", creature.getFullName(), disease.getName());
		} else {
			int heal = ActionType.DOCTOR_HEALS.getMoraleVariation();
			this.morale = Math.min(this.morale + heal, 100);

			int healCreature = ActionType.CREATURE_TREATED.getMoraleVariation();
			// creature.setMorale(Math.min(creature.getMorale() + healCreature, 100));
			creature.setMorale(Math.min(creature.getMorale() + healCreature, 100));

			// The value of the budget increase by healing the creature
			this.medicalService.setBudget(Math.min(this.medicalService.getBudget() + INCREASE_BUDGET_SERVICE, 100));
			log.info("Le médecin {} soigne la maladie {} de {} ! (+{} pts pour la créature et +{} pts pour le médecin)", this.getFullName(), disease.getName(), creature.getFullName(), healCreature, heal);
			log.info("Le soin fait augmenter le budget du service {} de {} points ({} pts)", this.medicalService.getName(), INCREASE_BUDGET_SERVICE, this.medicalService.getBudget());
		}
	}

	/**
	 * Transfers a group of creatures to a special service (Crypt or Quarantine) if the conditions are met.
	 * The method checks if the service has available beds and if the creatures are authorized to go there.
	 * @param service the medical service to which the creatures are transferred
	 * @param creaturesToTransfer the list of creatures to transfer
	 * @param waitingRoom the waiting room from which the creatures are transferred
	 * @return true if the transfer was successful, false otherwise
	 */
	private boolean transferToSpecialService(MedicalService service, List<Creature> creaturesToTransfer, Room waitingRoom) {
		if(service == null){
			return false;
		}
		if (service.getAvailableBeds() > 0) {
			if (service.getName().equals("Crypt")) {
				if (authorizedToGoToCrypt(creaturesToTransfer)) {
					if (!service.getCreatures().isEmpty()) {
						if (Objects.equals(creaturesToTransfer.getFirst().getRace(), service.getRoomType())) {
							transferGroup(creaturesToTransfer, waitingRoom, service, false);
							return true;
						}
						return false;
					}
					transferGroup(creaturesToTransfer, waitingRoom, service, false);
					return true;
				}
			}
			if (service.getName().equals("Quarantaine")) {
				if (authorizedToGoToQuarantine(creaturesToTransfer)) {
					if (!service.getCreatures().isEmpty()) {
						if (Objects.equals(creaturesToTransfer.getFirst().getRace(), service.getRoomType())) {
							transferGroup(creaturesToTransfer, waitingRoom, service, false);
							return true;
						}
						return false;
					}
					transferGroup(creaturesToTransfer, waitingRoom, service, false);
					return true;
				}
			}
		} else {
			return false;
		}
		return false;
	}

	/**
	 * Transfers a list of creature from one room to another if the conditions are met.
	 * All the creatures have to be the same race
	 * @param creatures the creatures to transfer
	 * @param roomFrom  the room to transfer from
	 * @param roomTo    the room to transfer to
	 */
	public void transferGroup(List<Creature> creatures, Room roomFrom, Room roomTo, boolean transferWaitingRoom) {
		//Check si il y a bien de la place dans la room de destination
		if (roomTo.getCreatures().size() >= roomTo.getMAX_CREATURE()) {
			return;
		}
		boolean isTransferPossible = true;
		// Vérification que la créature est bien dans la room
		for (Creature creature : creatures) {
			CopyOnWriteArrayList<Creature> creaturesRoom = roomFrom.getCreatures();

			if (roomTo.getCreatures().size() >= roomTo.getMAX_CREATURE()) {
				break;
			}
			if (!creaturesRoom.contains(creature)) {
				log.error("[medecin][transferer()] La créature {} à transférer n'est pas présente dans la room {}.", creature.getFullName(), roomFrom.getName());
				isTransferPossible = false;
			}

			String roomType = roomTo.getRoomType();
			//Si la room de destination n'est pas vide, on vérifie que la race de la creature correspond au type de la room
			// Sauf si c'est un transfert des creatures d'un service vers salle d'attente, param transferWaitingRoom à true (cas où service ferme)
			if (!roomTo.getCreatures().isEmpty() && !transferWaitingRoom) {
				if (!creature.getRace().equals(roomType)) {
					log.error("[medecin][transferer()] Transfert impossible, le type du service de destination ({}) n'est pas du type de la créature ({}).", roomType, creature.getRace());
					isTransferPossible = false;
				}
			}
			if (isTransferPossible) {
				roomFrom.removeCreature(creature);
				roomTo.addCreature(creature);
			}
		}
		if (transferWaitingRoom) {
			log.info("Le médecin {} transfère les créatures du service {} vers la salle d'attente", this.fullName, roomFrom.getName());
		} else {
			log.info("Le médecin {} transfère un groupe de {} de {} vers {}.", this.fullName, creatures.getFirst().getRace(), roomFrom.getName(), this.medicalService.getName());
		}
	}

	/**
	 * Checks the doctor's morale. If it reaches zero, the doctor leaves the service.
	 * Adds the doctor to the stack of doctors who died
	 * @return false if the doctor has left, true otherwise
	 */
	public boolean checkMorale() {
		if (this.morale == 0) {
			haraKiri();
			log.info("Le médecin {} a harakiri.", this.getFullName());
			Singleton instance = Singleton.getInstance();
			instance.addBeastToStack(this, StackType.DOCTOR);
			return false;
		}
		return true;
	}

	/**
	 * Applies depression to the doctor (lowers morale).
	 */
	public void depression() {
		int depression = ActionType.DOCTOR_DEPRESSION.getMoraleVariation();
		this.morale = Math.max(this.morale + depression, 0);
		log.info("Dépression, le médecin {} a maintenant {} de moral.", this.fullName, this.morale);
		notifyObservers();
	}

	/**
	 * Removes the doctor from their medical service.
	 */
	public void haraKiri() {
		this.harakiriTriggered = true;
		this.medicalService.removeDoctor(this);
		this.die(this.medicalService);
	}

	@Override
	public String toString() {
		return "[Médecin] nom='" + fullName + "', sexe='" + sex + "', âge=" + age + ", moral=" + morale + ", poids=" + weight + ", taille=" + height + "]";
	}

	/**
	 * Resets the doctor's state for a new turn.
	 */
	public void resetForNewTurn() {
		this.hasMovedThisTurn = false;
	}

}
