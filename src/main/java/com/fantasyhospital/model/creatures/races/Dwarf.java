package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.VIPPatient;
import com.fantasyhospital.model.disease.Disease;

import java.util.concurrent.CopyOnWriteArrayList;

public class Dwarf extends VIPPatient {

    public Dwarf() {
        super(null);
    }

    public Dwarf(CopyOnWriteArrayList<Disease> diseases) {
        super(diseases);
    }

}