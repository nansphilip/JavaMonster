package com.fantasyhospital.model.creatures;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fantasyhospital.Singleton;
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
	@Setter
	@Getter
	protected String race;

    @Getter
    @Setter
    private boolean harakiriTriggered = false;

	/**
	 * Medical service to which the doctor is assigned
	 */
	protected MedicalService medicalService;

	/**
	 * Liste d'observer qui sont appelés pour être notifiés d'un changement de moral du médecin
	 */
	private List<CreatureObserver> observers = new ArrayList<>();

    private boolean hasMovedThisTurn = false;

    // Constants of modification
    private static final int INCREASE_BUDGET_SERVICE = 5;

    /**
     * Constructs a doctor with their characteristics and assigned service.
     */
    public Doctor(String name, GenderType sex, int weight, int height, int age, int morale, String race, MedicalService medicalService) {
        super(name, sex, weight, height, age, morale);
        this.race = race;
        this.medicalService = medicalService;
        this.previousMorale = morale;
    }

	/**
	 * Doctor's waiting action (to be completed if needed).
	 */
	@Override
	public void waiting(Room room) {

	}

	/**
	 * Ajoute observer à la liste d'observer
	 *
	 * @param creatureObserver
	 */
	public void addObserver(CreatureObserver creatureObserver) {
		observers.add(creatureObserver);
	}

	/**
	 * Notifie les observer que l'état du médecin a changé
	 */
	public void notifyObservers() {
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

		// Si son service médical est vide, il essaie de transférer une créature de la room d'attente vers son service.
		// Il doit chercher un service du type de la créature, sinon choisir une autre créature.
		if (listeCreatures.isEmpty()) {
			Room waitingRoom = hospital.getRoomByName("Room d'attente");
			if (waitingRoom != null) {
				//Il reste des creatures dans la salle d'attente à soigner, il essaie d'en transférer le plus grand nombre de la même race.
				if (!waitingRoom.getCreatures().isEmpty()) {
					List<Creature> creaturesToTransfer = waitingRoom.getAllCreaturesOfSameRace();
					if (creaturesToTransfer != null) {
						if (authorizedToGoToCrypt(creaturesToTransfer)) {
							if (transferToSpecialService(hospital, "Crypt", creaturesToTransfer, waitingRoom)) {
								return;
							}
							if (!this.medicalService.getName().equals("Quarantaine")) {
								transferGroup(creaturesToTransfer, waitingRoom, this.medicalService);
								return;
							}
						}
						if (authorizedToGoToQuarantine(creaturesToTransfer)) {
							if (transferToSpecialService(hospital, "Quarantaine", creaturesToTransfer, waitingRoom)) {
								MedicalService quarantine = hospital.getMedicalServiceByName("Quarantaine");
								goTo(this.medicalService, quarantine);
								return;
							} else if (!this.medicalService.getName().equals("Crypt")) {
								transferGroup(creaturesToTransfer, waitingRoom, this.medicalService);
								return;
							}
						}
						if (!authorizedToGoToCrypt(creaturesToTransfer) && !authorizedToGoToQuarantine(creaturesToTransfer)) {
							transferGroup(creaturesToTransfer, waitingRoom, this.medicalService);
							return;
						}
					}
				}
				if (waitingRoom.getCreatures().isEmpty()) {
					//Si la salle d'attente est vide, le médecin va regarder dans les autres services s'il reste des créatures à soigner et va s'y rendre.
					//S'il ne reste qu'une créature dans l'hôpital (donc dans un service) et qu'il y a un médecin dans le service, il laisse l'autre médecin la soigner.
					for (MedicalService service : hospital.getMedicalServices()) {
						Creature creatureToHeal = service.getCreatureWithHighLevelDisease();
						if (creatureToHeal != null && service.getDoctors().isEmpty()) {
							goTo(this.medicalService, service);
							return;
						}
						if (creatureToHeal != null && hospital.getTotalCreaturesHospital() == 1 && !service.getDoctors().isEmpty()) {
							doctorWait();
							return;
						}
						if (creatureToHeal != null && service.getCreatures().size() > service.getDoctors().size()) {
							goTo(this.medicalService, service);
							return;
						}
					}
				}
			}
		}




        /* Room crypt = hospital.getRoomByName("Crypt");
        log.info("Lit disponibles dans la crypte : {}", crypt.getAvailableBeds());
        if (crypt.getAvailableBeds() > 0 && (creaturesToTransfer.getFirst().getRace().equals("Zombie") || creaturesToTransfer.getFirst().getRace().equals("Vampire"))) {
            if (Objects.equals(creaturesToTransfer.getFirst().getRace(), crypt.getRoomType())) {
                for (Creature creature : creaturesToTransfer) {
                    transfer(creature, waitingRoom, crypt);
                    log.info("La créature {} de race {} a été transférée dans la crypt !", creatureToTransfer.getFullName(), creatureToTransfer.getRace());
                }
                return;
            }
        } */

// Récupération de la créature avec le niveau de disease le plus avancé et le nombre de diseases le plus élevé
		Creature creatureMaxLvlDisease = this.medicalService.getCreatureWithHighLevelDisease();
		Creature creatureMaxDiseases = this.medicalService.getCreatureWithNbMaxDisease();

		if (creatureMaxDiseases == null || creatureMaxLvlDisease == null) {
			doctorWait();
			return;
		}

		//Soigne créature si niveau disease >= 8
		if (creatureMaxLvlDisease.getHighLevelDisease().getCurrentLevel() >= 8) {
			heal(creatureMaxLvlDisease, creatureMaxLvlDisease.getHighLevelDisease());
			return;
		} else if (creatureMaxDiseases.getDiseases().size() >= 3) {  //Soigne créature si nombre de disease >= 3
			heal(creatureMaxDiseases, creatureMaxDiseases.getHighLevelDisease());
			return;
		}

		heal(creatureMaxLvlDisease, creatureMaxLvlDisease.getHighLevelDisease());

	}


	public boolean authorizedToGoToCrypt(List<Creature> creatures) {
		return creatures.getFirst().getRace().equals("Zombie") || creatures.getFirst().getRace().equals("Vampire");
	}


	public boolean authorizedToGoToQuarantine(List<Creature> creatures) {
		if (creatures.getFirst().getRace().equals("Orc") || creatures.getFirst().getRace().equals("Werebeast") || creatures.getFirst().getRace().equals("Lycanthrope") || creatures.getFirst().getRace().equals("Vampire")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Déplace un docteur dans un autre service
	 *
	 * @param roomFrom le service dont part le medecin.
	 * @param roomTo   le service où va le médecin.
	 * @return true si ça a marché
	 */
	public void goTo(MedicalService roomFrom, MedicalService roomTo) {
		roomFrom.removeDoctor(this);
		roomTo.addDoctor(this);
		this.medicalService = roomTo;
		this.hasMovedThisTurn = true;
		log.info("Le docteur {} part du service {} et va dans le service {}", this.fullName, roomFrom.getName(), roomTo.getName());
	}

	public void doctorWait() {
		log.info("Le dr {} n'a rien à faire...", this);
	}

	/**
	 * Heals the disease of a creature with the highest level.
	 *
	 * @param creature the creature to heal
	 * @param disease  the disease to heal
	 */
	public void heal(Creature creature, Disease disease) {
		if (disease == null) {
			log.error("[medecin][soigner()] La créature {} n'a pas de disease", creature.getFullName());
			return;
		}

        // On récupère la room où se trouve la créature pour le passage à beCured
        MedicalService medicalService = this.medicalService;

        if (!creature.beCured(disease, medicalService)) {
            log.error("[medecin][soigner()] La créature {} ne possédait pas la disease {}", creature.getFullName(), disease.getName());
        } else {
            int heal = ActionType.DOCTOR_HEALS.getMoraleVariation();
            this.morale = Math.min(this.morale + heal, 100);

            int healCreature = ActionType.CREATURE_TREATED.getMoraleVariation();
            // creature.setMorale(Math.min(creature.getMorale() + healCreature, 100));
            creature.setMoraleWithRoom(Math.min(creature.getMorale() + healCreature, 100), medicalService);

            // The value of the budget increase by healing the creature
            medicalService.setBudget(Math.min(medicalService.getBudget() + INCREASE_BUDGET_SERVICE,100));
            log.info("Le médecin {} soigne la maladie {} de {} ! (+{} pts pour la créature et +{} pts pour le médecin)", this.getFullName(), disease.getName(), creature.getFullName(), healCreature, heal);
            log.info("Le soin fait augmenter le budget du service de {} points ({} pts)", medicalService.getName(), medicalService.getBudget());
        }
    }

	public boolean transferToSpecialService(Hospital hospital, String serviceName, List<Creature> creaturesToTransfer, Room waitingRoom) {
		Room room = hospital.getRoomByName(serviceName);
		if (room == null) {
			return false;
		}
		if (room.getAvailableBeds() > 0) {
			if (room.getName().equals("Crypt")) {
				if (creaturesToTransfer.getFirst().getRace().equals("Zombie") || creaturesToTransfer.getFirst().getRace().equals("Vampire")) {
					if (!room.getCreatures().isEmpty()) {
						if (Objects.equals(creaturesToTransfer.getFirst().getRace(), room.getRoomType())) {
							for (Creature creature : creaturesToTransfer) {
								transfer(creature, waitingRoom, room);
							}
							return true;
						} else {
							return false;
						}
					} else {
						for (Creature creature : creaturesToTransfer) {
							transfer(creature, waitingRoom, room);
						}
						return true;
					}
				}
			} else if (room.getName().equals("Quarantaine")) {

				if (Objects.equals(creaturesToTransfer.getFirst().getRace(), room.getRoomType())) {
					for (Creature creature : creaturesToTransfer) {
						transfer(creature, waitingRoom, room);
					}
					return true;
				}
			}
		} else {
			return false;
		}
		return false;
	}

	public boolean transferToSpecialService(Hospital hospital, String serviceName, Creature creatureToTransfer, Room waitingRoom) {
		Room room = hospital.getRoomByName(serviceName);
		if (room == null) {
			return false;
		}
		if (room.getAvailableBeds() > 0) {
			if (room.getName().equals("Crypt")) {
				if (creatureToTransfer.getRace().equals("Zombie") || creatureToTransfer.getRace().equals("Vampire")) {
					if (!room.getCreatures().isEmpty()) {
						if (Objects.equals(creatureToTransfer.getRace(), room.getRoomType())) {
							transfer(creatureToTransfer, waitingRoom, room);
							return true;
						} else {
							return false;
						}
					} else {
						transfer(creatureToTransfer, waitingRoom, room);
						log.info("La créature {} de race {} a été transférée dans la crypt !", creatureToTransfer.getFullName(), creatureToTransfer.getRace());
						log.info("Lit disponibles dans la {} : {}", room.getName(), room.getAvailableBeds());
						return true;
					}
				}
			} else if (room.getName().equals("Quarantaine")) {
				if (creatureToTransfer.getRace().equals("Orc") || creatureToTransfer.getRace().equals("Werebeast") || creatureToTransfer.getRace().equals("Lycanthrope") || creatureToTransfer.getRace().equals("Vampire"))
					if (Objects.equals(creatureToTransfer.getRace(), room.getRoomType())) {
						transfer(creatureToTransfer, waitingRoom, room);
						log.info("La créature {} de race {} a été transférée dans la crypt !", creatureToTransfer.getFullName(), creatureToTransfer.getRace());
						return true;
					}
			}
		}
		return false;
	}

	/**
	 * Revises the medical service's budget (to be completed).
	 */
	public void reviseBudget(int value) {
	}

	/**
	 * Transfers a creature from one room to another if the conditions are met.
	 *
	 * @param creature the creature to transfer
	 * @param roomFrom the room to transfer from
	 * @param roomTo   the room to transfer to
	 * @return true if the transfer was successful, false otherwise
	 */
	public boolean transfer(Creature creature, Room roomFrom, Room roomTo) {
		//Check si il y a bien de la place dans la room de destination
		if (roomTo.getCreatures().size() >= roomTo.getMAX_CREATURE()) {
			log.info("Le service de destination était déjà plein...");
			return false;
		}

		// Vérification que la créature est bien dans la room
		CopyOnWriteArrayList<Creature> creaturesRoom = roomFrom.getCreatures();
		if (!creaturesRoom.contains(creature)) {
			log.error("[medecin][transferer()] La créature {} à transférer n'est pas présente dans la room {}.", creature.getFullName(), roomFrom.getName());
			return false;
		}
		String roomType = roomTo.getRoomType();
		//Si la room de destination n'est pas vide, on vérifie que la race de la creature correspond au type de la room
		if (!roomTo.getCreatures().isEmpty()) {
			if (!creature.getRace().equals(roomType)) {
				log.error("[medecin][transferer()] Transfert impossible, le type du service de destination ({}) n'est pas du type de la créature ({}).", roomType, creature.getRace());
				return false;
			}
		}
		log.info("Le médecin {} transfère {} de {} vers {}.", this.fullName, creature.getFullName(), roomFrom.getName(), roomTo.getName());
		return roomFrom.removeCreature(creature) && roomTo.addCreature(creature);
	}

	/**
	 * Transfers a list of creature from one room to another if the conditions are met.
	 * All the creatures have to be the same race
	 *
	 * @param creatures the creatures to transfer
	 * @param roomFrom  the room to transfer from
	 * @param roomTo    the room to transfer to
	 * @return true if the transfer was successful, false otherwise
	 */
	public void transferGroup(List<Creature> creatures, Room roomFrom, Room roomTo) {
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
			if (!roomTo.getCreatures().isEmpty()) {
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
		log.info("Le médecin {} transfère un groupe de {} de {} vers {}.", this.fullName, creatures.getFirst().getRace(), roomFrom.getName(), this.medicalService.getName());
	}

	/**
	 * Checks the doctor's morale. If it reaches zero, the doctor leaves the service.
	 *
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
	private void haraKiri() {
		this.medicalService.removeDoctor(this);
	}

	@Override
	public String toString() {
		return "[Médecin] nom='" + fullName + "', sexe='" + sex + "', âge=" + age + ", moral=" + morale + ", poids=" + weight + ", taille=" + height + "]";
	}

	/**
	 * Réinitialise l'état du médecin pour un nouveau tour
	 */
	public void resetForNewTurn() {
		this.hasMovedThisTurn = false;
	}

}
