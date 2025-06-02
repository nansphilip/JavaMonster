package com.fantasyhospital.model.creatures.abstractclass;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fantasyhospital.Singleton;
import com.fantasyhospital.enums.ActionType;
import com.fantasyhospital.enums.StackType;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.model.rooms.Room;
import com.fantasyhospital.model.rooms.medicalservice.Quarantine;
import com.fantasyhospital.observer.CreatureObserver;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract class representing a creature in Fantasy Hospital. Manages diseases,
 * morale, reactions, and interactions with rooms.
 */
@Slf4j
public abstract class Creature extends Beast {

    /**
     * Set of diseases contracted by the creature
     */
    @Setter
    @Getter
    protected CopyOnWriteArrayList<Disease> diseases = new CopyOnWriteArrayList<>();

    /**
     * Shared random generator
     */
    protected static final Random RANDOM = new Random();

    /**
     * Number of howls the creature has made
     */
    private int howlCount;

    @Getter
    @Setter
    private boolean recentlyHealed = false;

    final private List<CreatureObserver> exitObservers = new ArrayList<>();
    final private List<CreatureObserver> moralObservers = new ArrayList<>();

    /**
     * Constructs a creature with an initial set of diseases.
     */
    public Creature(CopyOnWriteArrayList<Disease> diseases) {
        super();
        this.diseases = diseases;
        this.howlCount = 0;
    }

    /**
     * Makes the creature howl when morale is too low.
     */
    public void howl() {
        log.info("La créature {} a le moral dans les chaussettes, elle hurle.", this.fullName);
    }

    /**
     * Creature loses control and may contaminate others or die.
     * 30% chance to die when losing control.
     * Cannot contaminate if in quarantine.
     *
     * @param room the room where the creature is located
     * @return true if creature dies, false otherwise
     */
    public boolean loseControl(Room room) {
        if (room.getCreatures().isEmpty()) {
            return false;
        }

        // Check if creature is in quarantine
        if (room instanceof Quarantine) {
            log.info("La créature {} s'emporte, mais comme elle est en quarantaine, elle ne peut pas contaminer d'autres créatures.", this.fullName);
            // Can still die when losing control, even in quarantine
            if (Math.random() < 0.30) {
                log.info("La créature {} s'emporte trop fort, elle trépasse.", this.fullName);
                Singleton instanceSingleton = Singleton.getInstance();
                instanceSingleton.addBeastToStack(this, StackType.DIE);
                return true;
            }
            return false;
        }

        if (Math.random() < 0.30) {
            log.info("La créature {} s'emporte trop fort, elle trépasse.", this.fullName);
            Singleton instanceSingleton = Singleton.getInstance();
            instanceSingleton.addBeastToStack(this, StackType.DIE);
            return true;
        }

        // 15% chance to contaminate another creature when losing control
        if (Math.random() < 0.15) {
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
     * Checks creature's morale and triggers reactions if necessary.
     *
     * @param room the room where the creature is located
     * @return true if creature dies, false otherwise
     */
    public boolean checkMorale(Room room) {
        if (room == null) {
            return false;
        }

        // If creature is in quarantine, morale is frozen, do nothing
        if (room instanceof Quarantine) {
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
     * Checks creature's health (death if lethal disease or too many diseases).
     *
     * @param room the room where the creature is located
     * @return true if creature should leave the hospital (death or healed), false otherwise
     */
    public boolean hasCreatureToleaveHospital(Room room) {
        boolean creatureGetsOut = true;
        Singleton instanceSingleton = Singleton.getInstance();

        // Creature has no more diseases, it's healed
        if (this.diseases.isEmpty()) {
            instanceSingleton.addBeastToStack(this, StackType.HEAL);
            return true;
        }

        for (Disease disease : this.diseases) {
            if (disease.isLethal()) {
                log.info("La disease {} de {} était à son apogée.", disease.getName(), this.fullName);
                creatureGetsOut = die(room);
                if (creatureGetsOut) {
                    instanceSingleton.addBeastToStack(this, StackType.DIE);
                }
                return creatureGetsOut;
            }
        }
        if (this.diseases.size() >= 4) {
            log.info("{} a contracté trop de diseases.", this.fullName);
            creatureGetsOut = die(room);
            if (creatureGetsOut) {
                instanceSingleton.addBeastToStack(this, StackType.DIE);
            }
            return creatureGetsOut;
        }
        return false;
    }

    /**
     * Adds an observer for exit events.
     * 
     * @param creatureObserver the observer to add
     */
    public void addExitObserver(CreatureObserver creatureObserver) {
        this.exitObservers.add(creatureObserver);
    }

    /**
     * Adds an observer for morale events.
     * 
     * @param creatureObserver the observer to add
     */
    public void addMoralObserver(CreatureObserver creatureObserver) {
        this.moralObservers.add(creatureObserver);
    }

    /**
     * Notifies all exit observers.
     */
    public void notifyExitObservers() {
        for (CreatureObserver observer : this.exitObservers) {
            observer.onStateChanged(this);
        }
    }

    /**
     * Notifies all morale observers.
     */
    public void notifyMoralObservers() {
        for (CreatureObserver observer : this.moralObservers) {
            observer.onStateChanged(this);
        }
    }

    /**
     * Makes the creature contract a disease (or increases its level if already present).
     * 
     * @param disease the disease to contract
     */
    public void fallSick(Disease disease) {
        if (this.diseases.contains(disease)) {
            for (Disease diseaseAModifier : this.diseases) {
                if (diseaseAModifier.equals(disease)) {
                    diseaseAModifier.increaseLevel();
                }
            }
        } else {
            this.diseases.add(disease);
        }
        notifyExitObservers();
    }

    /**
     * Overrides setMorale to handle quarantine.
     * If creature is in quarantine, morale doesn't change.
     * 
     * @param morale new morale value
     */
    @Override
    public void setMorale(int morale) {
        // Using original method as we don't have access to room here
        // Quarantine check will be done by caller
        this.morale = morale;
    }

    /**
     * Changes creature's morale while considering quarantine.
     *
     * @param morale new morale value
     * @param room room where the creature is located
     */
    public void setMoraleWithRoom(int morale, Room room) {
        if (room instanceof Quarantine) {
            // In quarantine, morale is frozen, do nothing
            log.info("La créature {} est en quarantaine, son moral reste à {}.", this.fullName, this.morale);
            return;
        }

        // Otherwise, change morale normally
        this.morale = morale;
    }

    /**
     * Cures the creature of a given disease and gives morale points.
     *
     * @param disease the disease to cure
     * @return true if the disease was removed
     */
    public boolean beCured(Disease disease) {
        return beCured(disease, null);
    }

    /**
     * Cures the creature of a given disease and gives morale points.
     * Morale doesn't change if in quarantine.
     *
     * @param disease the disease to cure
     * @param room the room where the creature is located (to check for quarantine)
     * @return true if the disease was removed
     */
    public boolean beCured(Disease disease, Room room) {
        if (!this.diseases.contains(disease)) {
            return false;
        }

        // If creature is in quarantine, don't modify morale
        if (room == null || !(room instanceof Quarantine)) {
            this.morale = Math.min(this.morale + ActionType.CREATURE_TREATED.getMoraleVariation(), 100);
        }

        this.diseases.remove(disease);
        this.recentlyHealed = true;
        notifyExitObservers();
        return true;
    }

    /**
     * Finds the room where the creature is located.
     * This method is useful for quarantine checks.
     *
     * @return the room or null if not found
     */
    private Room findRoomOfCreature() {
        // Would need to traverse services to find where the creature is
        // This method would be better implemented with direct access to hospital
        // But for this simple implementation, we return null
        // (to be completed according to exact project architecture)
        return null;
    }

    /**
     * Returns the creature's race (name of the concrete class).
     * 
     * @return the race name
     */
    public String getRace() {
        return this.getClass().getSimpleName();
    }

    /**
     * Returns a random disease from the creature's diseases.
     * 
     * @return a random disease or null if no diseases
     */
    public Disease getRandomDisease() {
        if (this.diseases.isEmpty()) {
            log.error("La créature {} n'a aucune maladie.", this.fullName);
            return null;
        }
        Random random = new Random();
        return (Disease) this.diseases.toArray()[random.nextInt(this.diseases.size())];
    }

    /**
     * Returns the disease with the highest level.
     * 
     * @return the highest level disease or null if no diseases
     */
    public Disease getHighLevelDisease() {
        if (this.diseases.isEmpty()) {
            log.error("La créature {} n'a aucune disease.", this.fullName);
            return null;
        }
        Disease highLevelDisease = this.diseases.iterator().next();
        for (Disease disease : this.diseases) {
            if (disease.getCurrentLevel() > highLevelDisease.getCurrentLevel()) {
                highLevelDisease = disease;
            }
        }
        return highLevelDisease;
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
