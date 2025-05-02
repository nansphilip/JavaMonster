package com.fantasyhospital.salles.servicemedical;

public class Crypte extends ServiceMedical {
    private final int ventilation;
    private final double temperature;

    public Crypte(String nom, double superficie, int NB_CAPACITE_MAX, String budget, int ventilation, double temperature) {
        super(nom, superficie, NB_CAPACITE_MAX, budget);
        this.ventilation = ventilation;
        this.temperature = temperature;
    }

    // Getters et setters omis pour la clarté
} 