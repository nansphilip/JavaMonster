package com.fantasyhospital.model.creatures.abstractclass;

public abstract class Bete {

    protected String nomComplet;
    protected String sexe;
    protected int poids;
    protected int taille;
    protected int age;
    protected int moral;

    public Bete(String nomComplet, String sexe, int poids, int taille, int age, int moral) {
        this.nomComplet = nomComplet;
        this.sexe = sexe;
        this.poids = poids;
        this.taille = taille;
        this.age = age;
        this.moral = moral;
    }

    public abstract void attendre();

    public void trepasser(){
        //mourir
        System.out.println("je suis mort");
    }
}
