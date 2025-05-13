package com.fantasyhospital.model.creatures.abstractclass;

import com.fantasyhospital.Singleton;
import com.fantasyhospital.enums.ActionType;
import com.fantasyhospital.enums.StackType;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.observer.CreatureObserver;
import com.fantasyhospital.rooms.Room;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Abstract class representing a creature in Fantasy Hospital.
 * Manages diseases, morale, reactions, and interactions with rooms.
 */
@Slf4j
public abstract class Creature extends Beast {

    /**
     * Set of diseases contracted by the creature
     */
    @Setter @Getter protected CopyOnWriteArrayList<Disease> diseases = new CopyOnWriteArrayList<>();

    /**
     * Shared random generator
     */
    protected static final Random RANDOM = new Random();

    /**
     * Number of howl the beast has done
     */
    private int howlCount;

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
     * La créature s'emporte et peut contaminer une autre créature de la salle.
     * Elle a 30% de chance de trépasser en s'emportant
     * @return true si creature trepasse, false sinon
     */
    public boolean loseControl(Room room) {
        if (room.getCreatures().isEmpty()) {
            return false;
        }

        if(Math.random() < 0.30){
            log.info("La créature {} s'emporte trop fort, elle trépasse.", this.fullName);
            Singleton instanceSingleton = Singleton.getInstance();
            instanceSingleton.addBeastToStack(this, StackType.DIE);
            return true;
        }

        //15% de chance de contaminer creature lorsqu'il s'emporte
        if(Math.random() < 0.15){
            Creature creature = room.getRandomCreatureWithoutThisOne(this);
            Disease disease = this.getRandomDisease();

            if (disease == null || creature == null) {
                log.info("La créature {} s'emporte mais n'a aucune créature à contaminer...", this.fullName);
                return false;
            }

            creature.fallSick(disease);

            log.info("La créature {} s'emporte et contamine {} en lui transmettant {} dans la bagarre.", this.fullName, creature.fullName, disease.getName());
        }
        return false;
    }

    /**
     * Checks the creature's morale and triggers reactions if necessary.
     * @return true si la créature trépasse, false sinon
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
     * Checks the creature's health (death if lethal disease or too many diseases).
     *
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

    public void notifyExitObservers() {
        for (CreatureObserver observer : this.exitObservers) {
            observer.onStateChanged(this);
        }
    }

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
     * Cures the creature of a given disease and give 50 pts of moral
     * @return true if the disease was removed.
     */
    public boolean beCured(Disease disease) {
        if(!this.diseases.contains(disease)){
            return false;
        }
        this.morale = Math.min(this.morale + ActionType.CREATURE_TREATED.getMoraleVariation(), 100);
        this.diseases.remove(disease);
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
