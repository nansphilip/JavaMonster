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

@Setter @Getter @Slf4j public abstract class Beast {

    protected String fullName;
    protected GenderType sex;
    protected int weight;
    protected int height;
    protected int age;
    protected int morale;
    protected int previousMorale;

    public boolean isMoraleIncreasing() {
        return this.morale > previousMorale;
    }

    public boolean isMoraleDecreasing() {
        return this.morale < previousMorale;
    }


    public Beast() {
        this(null, generateRandomSex(), generateWeight(), generateHeight(), generateAge(), generateMorale());
        this.fullName = generateRandomName(this.sex);
    }

    public Beast(String fullName, GenderType sex, int weight, int height, int age, int morale) {
        this.fullName = fullName;
        this.sex = sex;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.morale = morale;
        this.previousMorale = morale;
    }

    public abstract void waiting(Room room);

    public boolean die(Room room) {
        log.info("La créature {} se meurt.", this.fullName);
        return true;
    }

}

