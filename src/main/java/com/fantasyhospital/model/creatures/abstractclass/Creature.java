package com.fantasyhospital.model.creatures.abstractclass;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.rooms.Room;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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
    private int howlCount;

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
     * The creature loses control and may infect another creature in the room.
     */
    public void loseControl(Room room) {
        if (room.getCreatures().isEmpty()) {
            return;
        }
        double rnd = new Random().nextDouble();
        //15% de chance de contaminer creature lorsqu'il s'emporte
        if(rnd < 0.15){
            Creature creature = room.getRandomCreatureWithoutThisOne(this);
            Disease disease = this.getRandomDisease();

            if (disease == null || creature == null) {
                log.info("La créature {} s'emporte mais n'a aucune créature à contaminer...", this.fullName);
                return;
            }

            creature.fallSick(disease);

            log.info("La créature {} s'emporte et contamine {} en lui transmettant {} dans la bagarre.", this.fullName, creature.fullName, disease.getName());
        }
    }

    /**
     * Checks the creature's morale and triggers reactions if necessary.
     */
    public void checkMorale(Room room) {
        if (this.howlCount > 2) {
            loseControl(room);
            return;
        }
        if (this.morale == 0) {
            howl();
            this.howlCount++;
        } else {
            this.howlCount = 0;
        }
    }

    /**
     * Checks the creature's health (death if lethal disease or too many diseases).
     *
     * @return true if the creature should leave the room (death), false otherwise.
     */
    public boolean hasCreatureToleaveHospital(Room room){
        boolean creatureGetsOut = true;
        if(this.diseases.isEmpty()){
            return true;
        }
        for(Disease disease : this.diseases){
            if(disease.isLethal()){
                log.info("La disease {} de {} était à son apogée.", disease.getName(), this.fullName);
                creatureGetsOut = die(room);
                return creatureGetsOut;
            }
        }
        if(this.diseases.size() >= 4){
            log.info("{} a contracté trop de diseases.", this.fullName);
            creatureGetsOut = die(room);
            return creatureGetsOut;
        }
        // TODO: Rajouter 30% chance trepasser quand il s'emporte
        return false;
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
    }

    /**
     * Cures the creature of a given disease.
     *
     * @return true if the disease was removed.
     */
    public boolean beCured(Disease disease) {
        return this.diseases.remove(disease);
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
