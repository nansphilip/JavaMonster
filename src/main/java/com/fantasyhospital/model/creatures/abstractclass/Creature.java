package com.fantasyhospital.model.creatures.abstractclass;

import com.fantasyhospital.model.maladie.Maladie;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Creature extends Bete {
    protected List<Maladie> maladies = new ArrayList<>();
    public static Random random = new Random();

    public Creature(String nomComplet, String sexe, int poids, int taille, int age, int moral, List<Maladie> maladies) {
        super(nomComplet, sexe, poids, taille, age, moral);
        this.maladies = maladies;
    }

    public static String genererNomAleatoire() {
        String[] prefix = {"Kra", "Zor", "El", "Thra", "Gor", "Vel", "Mor", "Sha", "Lun", "Dra"};
        String[] suffix = {"gor", "nax", "iel", "dor", "vak", "mir", "thar", "dil", "rak", "zul"};

        return prefix[random.nextInt(prefix.length)] + suffix[random.nextInt(suffix.length)];
    }

    public static String genererSexeAleatoire() {
        String[] sexes = {"M", "F"};
        return sexes[random.nextInt(sexes.length)];
    }

    public static int genererPoids(){
        return (int) (Math.round((50.0 + random.nextDouble() * 50.0) * 10.0) / 10.0);
    }

    public static int genererTaille(){
        return (int) ((int) Math.round((150.0 + random.nextDouble() * 50.0) * 10.0) / 10.0);
    }

    public static int genererAge(){
        return 18 + random.nextInt(60);
    }

    public static int genererMoral(){
        return 60 + random.nextInt(40);
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
    public String getRace() {
        return this.getClass().getSimpleName();
    }

    public List<Maladie> getMaladies() {
        return maladies;
    }

    public void setMaladies(List<Maladie> maladies) {
        this.maladies = maladies;
    }

    @Override
    public String toString() {
        return "[" + getRace() + "] nom='" + nomComplet + "', sexe='" + sexe + "', âge=" + age + ", moral=" + moral + ", maladie(s) : " + this.maladies;
    }
} 