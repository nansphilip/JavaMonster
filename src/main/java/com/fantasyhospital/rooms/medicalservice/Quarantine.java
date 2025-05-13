package com.fantasyhospital.rooms.medicalservice;

public class Quarantine extends MedicalService {

    private final boolean isolation;

    public Quarantine(String name, double area, int MAX_CAPACITY, String budget, boolean isolation) {
        super(name, area, MAX_CAPACITY, budget);
        this.isolation = isolation;
    }
}
