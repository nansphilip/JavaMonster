package com.fantasyhospital.rooms.medicalservice;

import com.fantasyhospital.enums.BudgetType;

public class Crypt extends MedicalService {

    private final int ventilation;
    private final double temperature;

    public Crypt(String name, double area, int MAX_CAPACITY, BudgetType budgetType, int ventilation, double temperature) {
        super(name, area, MAX_CAPACITY, budgetType);
        this.ventilation = ventilation;
        this.temperature = temperature;
    }

    // Getters et setters omis pour la clarté
}
