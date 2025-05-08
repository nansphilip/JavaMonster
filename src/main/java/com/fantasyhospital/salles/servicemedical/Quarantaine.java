package com.fantasyhospital.salles.servicemedical;

public class Quarantaine extends ServiceMedical {

    private final boolean isolation;

    public Quarantaine(String nom, double superficie, int NB_CAPACITE_MAX, String budget, boolean isolation) {
        super(nom, superficie, NB_CAPACITE_MAX, budget);
        this.isolation = isolation;
    }
}
