package com.fantasyhospital.model.creatures.abstractclass;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fantasyhospital.enums.ActionType;
import com.fantasyhospital.enums.StackType;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.model.rooms.Room;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import com.fantasyhospital.model.rooms.medicalservice.Quarantine;
import com.fantasyhospital.observer.CreatureObserver;
import com.fantasyhospital.util.Singleton;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract class representing a creature in Fantasy Hospital.
 * Manages diseases, morale, reactions, and interactions with rooms.
 */
@Slf4j
@Getter @Setter
public abstract class Creature extends Beast {

    // Constants
    private static final double CONTAMINATE_WHEN_LOSING_CONTROL_CHANCE = 0.3;
    private static final double TREPASSING_CHANCE = 0.4;

    /**
     * Set of diseases contracted by the creature
     */
    protected CopyOnWriteArrayList<Disease> diseases = new CopyOnWriteArrayList<>();

    /**
     * Number of howl the beast has done
     */
    private int howlCount;

    /**
     * Boolean, true if the creature has been recently healed, false otherwise
     */
    private boolean recentlyHealed = false;

    // Attributes linked to observers
    private List<CreatureObserver> exitObservers = new ArrayList<>();
    private List<CreatureObserver> moralObservers = new ArrayList<>();

    /**
     * Constructs a creature with an initial set of diseases.
     */
    public Creature( CopyOnWriteArrayList<Disease> diseases) {
        super();
        this.diseases = diseases;
        this.howlCount = 0;
    }

    /**
     * Makes the creature howl if its morale is too low.
     */
    public void howl() {
        log.info("La créature {} a le moral dans les chaussettes, elle hurle.", this.fullName);
    }

    /**
     * The creature lose control and can contaminate an other creature in the room
     * It has 40% chance of trepassing while losing control
     * If it is in quarantine, it can't contaminate an other creature
     * @return true if creature trepasses, false otherwise
     */
    private boolean loseControl(Room room) {
        if (room.getCreatures().isEmpty()) {
            return false;
        }

        // Vérifier si la créature est en quarantaine
        if (room instanceof Quarantine) {
            log.info("La créature {} s'emporte, mais comme elle est en quarantaine, elle ne peut pas contaminer d'autres créatures.", this.fullName);
            // Elle peut toujours mourir en s'emportant, même en quarantaine
            if(Math.random() < TREPASSING_CHANCE){
                log.info("La créature {} s'emporte trop fort, elle trépasse.", this.fullName);
                Singleton instanceSingleton = Singleton.getInstance();
                instanceSingleton.addBeastToStack(this, StackType.DIE);
                return true;
            }
            return false;
        }

        if(Math.random() < TREPASSING_CHANCE){
            log.info("La créature {} s'emporte trop fort, elle trépasse.", this.fullName);
            Singleton instanceSingleton = Singleton.getInstance();
            instanceSingleton.addBeastToStack(this, StackType.DIE);
            return true;
        }

        //30% de chance de contaminer creature lorsqu'il s'emporte
        if(Math.random() < CONTAMINATE_WHEN_LOSING_CONTROL_CHANCE){
            Creature creature = room.getRandomCreatureWithoutThisOne(this);
            Disease disease = this.getRandomDisease();

            if (disease == null || creature == null) {
                log.info("La créature {} s'emporte mais n'a aucune créature à contaminer...", this.fullName);
                return false;
            }

            disease.setCurrentLevel(1);
            creature.fallSick(disease);

            log.info("La créature {} s'emporte et contamine {} en lui transmettant {} dans la bagarre.", this.fullName, creature.fullName, disease.getName());
        }
        return false;
    }

    /**
     * Checks the creature's morale and triggers reactions if necessary.
     * @return true if creature trepasses, false otherwise
     */
    public boolean checkMorale(Room room) {
        if(room == null){
            return false;
        }

        if (this.howlCount > 2) {
            return loseControl(room);
        }
        if (this.morale == 0) {
            howl();
            this.howlCount++;
        } else {
            this.howlCount = 0;
        }
        return false;
    }

    /**
     * Checks the creature's health (it dies if it has a lethal disease or too many diseases).
     * @return true if the creature should leave the room (death), false otherwise.
     */
    public boolean hasCreatureToleaveHospital(Room room){
        boolean creatureGetsOut = true;
        Singleton instanceSingleton = Singleton.getInstance();

        //Créature n'a plus de maladies, elle est soignée
        if(this.diseases.isEmpty()){
            instanceSingleton.addBeastToStack(this, StackType.HEAL);
            return true;
        }

        for(Disease disease : this.diseases){
            if(disease.isLethal()){
                log.info("La disease {} de {} était à son apogée.", disease.getName(), this.fullName);
                creatureGetsOut = die(room);
                if(creatureGetsOut){
                    instanceSingleton.addBeastToStack(this, StackType.DIE);
                }
                return creatureGetsOut;
            }
        }
        if(this.diseases.size() >= 4){
            log.info("{} a contracté trop de diseases.", this.fullName);
            creatureGetsOut = die(room);
            if(creatureGetsOut){
                instanceSingleton.addBeastToStack(this, StackType.DIE);
            }
            return creatureGetsOut;
        }
        return false;
    }

    public void addExitObserver(CreatureObserver creatureObserver){
        this.exitObservers.add(creatureObserver);
    }

    public void addMoralObserver(CreatureObserver creatureObserver){
        this.moralObservers.add(creatureObserver);
    }

    /**
     * Method that notifies the exit observers when a creature can possibly die or when healed (and so have to leave hospital)
     */
    public void notifyExitObservers() {
        for (CreatureObserver observer : this.exitObservers) {
            observer.onStateChanged(this);
        }
    }

    /**
     * Method that notifies the moral observers when the moral of a creature changes
     */
    public void notifyMoralObservers() {
        for (CreatureObserver observer : this.moralObservers) {
            observer.onStateChanged(this);
        }
    }

    /**
     * Causes the creature to contract a disease (or increases its level if already present).
     */
    public void fallSick(Disease disease){
        if(this.diseases.contains(disease)){
            for(Disease diseaseAModifier : this.diseases){
                if(diseaseAModifier.equals(disease)){
                    diseaseAModifier.increaseLevel();
                }
            }
        } else {
            this.diseases.add(disease);
        }
        notifyExitObservers();
    }

    /**
     * method of dying
     *
     * @param room the room where the beast is dying
     * @return true if the creature gets out of the hospital, false otherwise (in the case of regenerating interface)
     */
    @Override
    public boolean die(Room room) {
        return true;
    }

    /**
     * Cures the creature of a given disease and give moral points
     * (moral doesn't change if in quarantine)
     * @param disease The disease to be cured
     * @param medicalService The medical service where the creature is curently
     * @return true if the disease was removed, false it had not the disease
     */
    public boolean beCured(Disease disease, MedicalService medicalService) {
        if(!this.diseases.contains(disease)){
            return false;
        }

        // Si la créature est en quarantaine, on ne modifie pas son moral
        if (medicalService == null || !(medicalService instanceof Quarantine)) {
            this.morale = Math.min(this.morale + ActionType.CREATURE_TREATED.getMoraleVariation(), 100);
        }

        this.diseases.remove(disease);
        this.recentlyHealed = true;
        notifyExitObservers();
        return true;
    }

    /**
     * Returns the creature's race (name of the concrete class).
     */
    public String getRace() {
        return this.getClass().getSimpleName();
    }

    /**
     * Returns a random disease from the creature's diseases.
     */
    public Disease getRandomDisease(){
        if(this.diseases.isEmpty()){
            log.error("La créature {} n'a aucune disease.", this.fullName);
            return null;
        }
        Random random = new Random();
        return (Disease) this.diseases.toArray()[random.nextInt(this.diseases.size())];
    }

    /**
     * Returns the disease with the highest level.
     */
    public Disease getHighLevelDisease() {
        if (this.diseases.isEmpty()) {
            log.error("La créature {} n'a aucune disease.", this.fullName);
            return null;
        }
        Disease highLevelDisease  = this.diseases.iterator().next();
        for (Disease disease : this.diseases) {
            if (disease.getCurrentLevel() > highLevelDisease .getCurrentLevel()) {
                highLevelDisease  = disease;
            }
        }
        return highLevelDisease ;
    }

    @Override
    public String toString() {
        return "[" + getRace() + "] nom='" + fullName + "', sexe='" + sex + "', âge=" + age + ", moral=" + morale + ", disease(s) : " + this.diseases;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Creature creature = (Creature) o;
        return Objects.equals(fullName, creature.fullName) && Objects.equals(height, creature.height) && Objects.equals(weight, creature.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, height, weight);
    }
}
