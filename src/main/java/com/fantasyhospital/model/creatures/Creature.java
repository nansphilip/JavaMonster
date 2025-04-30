package com.fantasyhospital.model.creatures;

import com.fantasyhospital.model.maladie.Maladie;

import java.util.ArrayList;
import java.util.List;

public abstract class Creature extends Bete {
    protected List<Maladie> maladies = new ArrayList<>();

    public Creature(String nomComplet, String sexe, int poids, int taille, int age, int moral, List<Maladie> maladies) {
        super(nomComplet, sexe, poids, taille, age, moral);
        this.maladies = maladies;
    }

    public void hurler(){

    }
    public void semporter(){

    }
    public void tomberMalade(Maladie maladie){

    }
    public void etreSoigne(){

    }

    // Getters et setters omis pour la clarté
} 