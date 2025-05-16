package com.fantasyhospital.model.creatures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
import com.fantasyhospital.rooms.Room;
import com.fantasyhospital.rooms.medicalservice.MedicalService;

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

    /**
     * Medical service to which the doctor is assigned
     */
    protected MedicalService medicalService;

    /**
     * Liste d'observer qui sont appelés pour être notifiés d'un changement de moral du médecin
     */
    private List<CreatureObserver> observers = new ArrayList<>();

    /**
     * Constructs a doctor with their characteristics and assigned service.
     */
    public Doctor(String name, GenderType sex, int weight, int height, int age, int morale, String race, MedicalService medicalService) {
        super(name, sex, weight, height, age, morale);
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
     * Ajoute un observer à la liste d'observer
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
        if(hospital.getTotalCreaturesHospital() == 0){
            return;
        }
        CopyOnWriteArrayList<Creature> listeCreatures = this.medicalService.getCreatures();

        // Si son service médical est vide, il essaie de transférer une créature de la room d'attente vers son service
        // Il doit chercher un service du type de la créature, sinon choisir une autre créature
        if(listeCreatures.isEmpty()){
            Room waintingRoom = hospital.getRoomByName("Room d'attente");
            if(waintingRoom != null){
                //Il reste des creatures dans la salle d'attente à soigner, il essaie d'en transferer le plus grand nombre de la même race
                if(!waintingRoom.getCreatures().isEmpty()){
                    Creature creatureToTransfer = waintingRoom.getCreatureWithHighLevelDisease();
                    List<Creature> creaturesToTransfer = waintingRoom.getAllCreaturesOfSameRace();
                    if(creaturesToTransfer != null){
                        transferGroup(creaturesToTransfer, waintingRoom, this.medicalService);
                        log.info("Le médecin {} transfère un groupe de {} de {} vers {}.", this.fullName, creaturesToTransfer.get(0).getRace(), waintingRoom.getName(), this.medicalService.getName());
                    } else {
                        if(transfer(creatureToTransfer, waintingRoom, this.medicalService)){
                            log.info("Le médecin {} transfère la créature {} de {} vers {}.", this.fullName, creatureToTransfer.getFullName(), waintingRoom.getName(), this.medicalService.getName());
                        }
                    }
                    return;
                } else {
                    //si la salle d'attente est vide, le médecin va regarder dans les autres services si il reste des créatures à soigner et la transferer
                    Creature creatureToTransfer = null;
                    for(MedicalService service : hospital.getMedicalServices()){
                        creatureToTransfer = service.getCreatureWithHighLevelDisease();
                        if(creatureToTransfer != null){
                            if(transfer(creatureToTransfer, service, this.medicalService)){
                                log.info("Le médecin {} transfère la créature {} de {} vers {}.", this.fullName, creatureToTransfer.getFullName(), service.getName(), this.medicalService.getName());
                            }
                            return;
                        }
                    }
                }
            }
        }

        // Récupération de la créature avec le niveau de disease le plus avancé et le nombre de diseases le plus élevé
        Creature creatureMaxLvlDisease = this.medicalService.getCreatureWithHighLevelDisease();
        Creature creatureMaxDiseases = this.medicalService.getCreatureWithNbMaxDisease();

        //Soigne créature si niveau disease >= 8
        if(creatureMaxLvlDisease.getHighLevelDisease().getCurrentLevel() >= 8){
            heal(creatureMaxLvlDisease, creatureMaxLvlDisease.getHighLevelDisease());
            return;
        } else if(creatureMaxDiseases.getDiseases().size() >= 3) {  //Soigne créature si nombre de disease >= 3
            heal(creatureMaxDiseases, creatureMaxDiseases.getHighLevelDisease());
            return;
        }
        heal(creatureMaxLvlDisease, creatureMaxLvlDisease.getHighLevelDisease());
    }

    /**
     * Heals the disease of a creature with the highest level.
     *
     * @param creature the creature to heal
     * @param disease the disease to heal
     */
    public void heal(Creature creature, Disease disease) {
        if (disease == null) {
            log.error("[medecin][soigner()] La créature {} n'a pas de disease", creature.getFullName());
            return;
        }
        if (!creature.beCured(disease)) {
            log.error("[medecin][soigner()] La créature {} ne possédait pas la disease {}", creature.getFullName(), disease.getName());
        } else {
            int heal = ActionType.DOCTOR_HEALS.getMoraleVariation();
            this.morale = Math.min(this.morale + heal, 100);

            int healCreature = ActionType.CREATURE_TREATED.getMoraleVariation();
            creature.setMorale(Math.min(creature.getMorale() + healCreature, 100));
            //for(Creature creatureService : this.medicalService.getCreatures()){
                //creatureService.setMorale(Math.min(creatureService.getMorale() + healCreature, 100));
            //}
            log.info("Le médecin {} soigne la maladie {} de {} ! (+{} pts pour la créature et +{} pts pour le médecin)", this.getFullName(), disease.getName(), creature.getFullName(), healCreature, heal);
            //log.info("Soigner a redonné {} points de moral au médecin {} et {} points à toutes les créatures du service. Moral actuel du médecin : {}", heal, this.getFullName(), healCreature, this.morale);
        }
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
     * @param roomTo the room to transfer to
     * @return true if the transfer was successful, false otherwise
     */
    public boolean transfer(Creature creature, Room roomFrom, Room roomTo) {
        //Check si il y a bien de la place dans la room de destination
        if(roomTo.getCreatures().size() >= roomTo.getMAX_CREATURE()){
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
        return roomFrom.removeCreature(creature) && roomTo.addCreature(creature);
    }

    /**
     * Transfers a list of creature from one room to another if the conditions are met.
     * All the creatures have to be the same race
     *
     * @param creatures the creatures to transfer
     * @param roomFrom the room to transfer from
     * @param roomTo the room to transfer to
     * @return true if the transfer was successful, false otherwise
     */
    public void transferGroup(List<Creature> creatures, Room roomFrom, Room roomTo) {
        //Check si il y a bien de la place dans la room de destination
        if(roomTo.getCreatures().size() >= roomTo.getMAX_CREATURE()){
            log.info("Le service de destination était déjà plein...");
            return;
        }
        boolean isTransferPossible = true;
        // Vérification que la créature est bien dans la room
        for(Creature creature : creatures){
            CopyOnWriteArrayList<Creature> creaturesRoom = roomFrom.getCreatures();

            if(roomTo.getCreatures().size() >= roomTo.getMAX_CREATURE()) {
                log.info("Le service de destination était déjà plein...");
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
            if(isTransferPossible){
                roomFrom.removeCreature(creature);
                roomTo.addCreature(creature);
            }
        }
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
}
