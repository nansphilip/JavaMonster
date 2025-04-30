package com.fantasyhospital.model.creatures;

import java.util.List;

import com.fantasyhospital.model.Creature;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.model.interfaces.Bestial;
import com.fantasyhospital.model.interfaces.Demoralisateur;
import com.fantasyhospital.model.interfaces.Regenerant;
import com.fantasyhospital.model.interfaces.VIP;

public class Vampire extends Creature implements Bestial, Regenerant, Demoralisateur, VIP {
    public Vampire(String nom, String sexe, double poids, double taille, int age) {
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
    public void trepasser() { 
        /* 
        contaminer();
    ... */ }

    @Override
    public void contaminer(Creature cible) { /* ... */ }
    @Override
    public void regenerer() { /* ... */ }
    // est ce qu'on a besoin de la méthode estContagieux car méthode contaminer appelée dans trepasser()
    @Override
    public boolean estContagieux() { return true; }
    @Override
    public void traitementVIP() { /* ... */ }
    @Override
    public void demoraliser(List<Creature> cibles) { /* ... */ }
} 