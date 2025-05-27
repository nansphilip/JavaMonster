package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.VIPPatient;
import com.fantasyhospital.model.creatures.interfaces.Contaminant;
import com.fantasyhospital.model.creatures.interfaces.Demoralizing;
import com.fantasyhospital.model.creatures.interfaces.Regenerating;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.model.rooms.Room;
import com.fantasyhospital.model.rooms.medicalservice.Crypt;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class Vampire extends VIPPatient implements Regenerating, Contaminant, Demoralizing {

    /**
     * True if the vampire has already regenerate once, false otherwise (particular rule of the crypt)
     */
    public boolean hasRegenerate = false;

    public Vampire() {
        super(null);
    }

    public Vampire(CopyOnWriteArrayList<Disease> diseases) {
        super(diseases);
    }

    //    public Vampire(String nomComplet, String sexe, int poids, int taille, int age, int moral, HashSet<Disease> diseases) {
    //        super(nomComplet, sexe, poids, taille, age, moral, diseases);
    //    }

    @Override
    public boolean die(Room room) {
        if(hasRegenerate && room instanceof Crypt) {
            log.info("La créature {} avait déjà regénéré une fois, la crypte est implacable..",  this.fullName);
            return true;
        }
        super.die(room);
        contaminate(this, room);
        demoralize(this, room);
        hasRegenerate = true;
        return regenerate(this);
    }
}