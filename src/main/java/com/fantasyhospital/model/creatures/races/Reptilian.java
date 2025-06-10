package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.VIPPatient;
import com.fantasyhospital.model.disease.Disease;

import java.util.concurrent.CopyOnWriteArrayList;

public class Reptilian extends VIPPatient {

    public Reptilian() {
        super(null);
    }

    public Reptilian(CopyOnWriteArrayList<Disease> diseases) {
        super(diseases);
    }

}