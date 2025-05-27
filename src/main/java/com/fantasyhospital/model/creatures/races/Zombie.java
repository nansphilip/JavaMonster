package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.TriageResident;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.model.creatures.interfaces.Regenerating;
import com.fantasyhospital.model.rooms.Room;
import com.fantasyhospital.model.rooms.medicalservice.Crypt;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j @Getter
public class Zombie extends TriageResident implements Regenerating {

    /**
     * True if the vampire has already regenerate once, false otherwise (particular rule of the crypt)
     */
    public boolean hasRegenerate = false;

    public Zombie() {
        super(null);
    }

    public Zombie(CopyOnWriteArrayList<Disease> diseases) {
        super(diseases);
    }

//    public Zombie(String nom, String sexe, int poids, int taille, int age, int moral, HashSet<Disease> diseases) {
//        super(nom, sexe, poids, taille, age, moral, diseases);
//    }

    @Override
    public boolean die(Room room) {
        if(hasRegenerate && room instanceof Crypt) {
            log.info("La créature {} avait déjà regénéré une fois, la crypte est implacable..",  this.fullName);
            return true;
        }
        super.die(room);
        hasRegenerate = true;
        return regenerate(this);
    }
}