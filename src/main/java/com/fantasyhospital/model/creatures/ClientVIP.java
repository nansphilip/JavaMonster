package com.fantasyhospital.model.creatures;

public abstract class ClientVIP extends Creature {

    public ClientVIP(String nom, String sexe, double poids, double taille, int age) {
        super(nom, sexe, poids, taille, age);
    }

    public void attendre(){
        //moral tombe au plus bas si attend trop longtemps
    }
}
