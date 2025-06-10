package com.fantasyhospital.model.creatures.abstractclass;

import com.fantasyhospital.enums.GenderType;
import com.fantasyhospital.model.rooms.Room;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import static com.fantasyhospital.model.creatures.abstractclass.BeastUtils.generateAge;
import static com.fantasyhospital.model.creatures.abstractclass.BeastUtils.generateMorale;
import static com.fantasyhospital.model.creatures.abstractclass.BeastUtils.generateRandomName;
import static com.fantasyhospital.model.creatures.abstractclass.BeastUtils.generateWeight;
import static com.fantasyhospital.model.creatures.abstractclass.BeastUtils.generateRandomSex;
import static com.fantasyhospital.model.creatures.abstractclass.BeastUtils.generateHeight;

/**
 * Abstract class representing a beast in Fantasy Hospital, which can be a doctor or a creature
 * Contains attributes and methods common to all beasts.
 */
@Setter @Getter @Slf4j public abstract class Beast {

    protected String fullName;
    protected GenderType sex;
    protected int weight;
    protected int height;
    protected int age;
    protected int morale;
    protected int previousMorale;

    /**
     * Default constructor of a Beast
     * @param fullName
     * @param sex
     * @param weight
     * @param height
     * @param age
     * @param morale
     */
    public Beast(String fullName, GenderType sex, int weight, int height, int age, int morale) {
        this.fullName = fullName;
        this.sex = sex;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.morale = morale;
        this.previousMorale = morale;
    }

    /**
     * Constructor with no param used to generate a random Beast, by using the utils methods in BeastUtils that generates random values needed to construct a Beast
     */
    public Beast() {
        this(null, generateRandomSex(), generateWeight(), generateHeight(), generateAge(), generateMorale());
        this.fullName = generateRandomName(this.sex);
    }

    /**
     * Method that compare the actual morale to the previous morale
     * @return true if the morale has increased, false if it has decreased
     */
    public boolean isMoraleIncreasing() {
        return this.morale > previousMorale;
    }

    public boolean isMoraleDecreasing() {
        return this.morale < previousMorale;
    }

    /**
     * Abstract method of waiting, that is implemented in the inherited classes
     * @param room the room where the beast is waiting
     */
    public abstract void waiting(Room room);

    /**
     * Abstract method of dying
     * @param room the room where the beast is dying
     * @return true if the creature gets out of the hospital, false otherwise (in the case of regenerating interface)
     */
    public boolean die(Room room) {
        log.info("La créature {} vient de mourir...", this.fullName);
        return true;
    }
}

