package com.fantasyhospital.model.creatures;

import java.util.List;

import com.fantasyhospital.model.Creature;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.model.interfaces.Demoralisateur;
import com.fantasyhospital.model.interfaces.VIP;


public class Elfe extends Creature implements VIP, Demoralisateur {
    public Elfe(String nom, String sexe, double poids, double taille, int age) {
        super(nom, sexe, poids, taille, age);
    }

    @Override
    public void attendre() { /* ... */ }
    @Override
    public void hurler() { /* ... */ }
    @Override
    public void semporter() { /* ... */ }
    @Override
    public void tomberMalade(Maladie maladie) { /* ... */ }
    @Override
    public void soigner() { /* ... */ }
    @Override
    public void trepasser() { /* 
        //demoraliser();
    ... */ }

    @Override
    public void traitementVIP() { /* ... */ }
    @Override
    public void demoraliser(List<Creature> cibles) { /* ... */ }
}
