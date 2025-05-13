package com.fantasyhospital.rooms.medicalservice;

public class Crypt extends MedicalService {

    private final int ventilation;
    private final double temperature;

    public Crypt(String name, double area, int MAX_CAPACITY, String budget, int ventilation, double temperature) {
        super(name, area, MAX_CAPACITY, budget);
        this.ventilation = ventilation;
        this.temperature = temperature;
    }

    // Getters et setters omis pour la clarté
}
