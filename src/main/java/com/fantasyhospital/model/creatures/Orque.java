package com.fantasyhospital.model.creatures;

import com.fantasyhospital.model.interfaces.Contaminant;
import com.fantasyhospital.model.maladie.Maladie;

import java.util.List;

public class Orque extends HabitantTriage implements Contaminant {
    public Orque(String nom, String sexe, int poids, int taille, int age, int moral, List<Maladie> maladies) {
        super(nom, sexe, poids, taille, age, moral, maladies);
    }
}