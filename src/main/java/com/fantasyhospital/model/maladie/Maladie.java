package com.fantasyhospital.model.maladie;

import java.util.*;

public class Maladie {
    protected String nomComplet;
    protected String nomAbrege;
    protected int NIVEAU_MAX;
    protected int niveauActuel;

    public static Random random = new Random();

    public Maladie(){
        Map.Entry<String, String> randomMaladie = randomNomMaladie();
        this.nomAbrege = randomMaladie.getKey();
        this.nomComplet = randomMaladie.getValue();
        this.NIVEAU_MAX = genererNiveauMaxAleatoire();
        this.niveauActuel = 1;
    }

    public Maladie(String nomComplet, String nomAbrege, int NIVEAU_MAX, int niveauActuel) {
        this.nomComplet = nomComplet;
        this.nomAbrege = nomAbrege;
        this.NIVEAU_MAX = NIVEAU_MAX;
        this.niveauActuel = niveauActuel;
    }

    public static Map.Entry<String, String> randomNomMaladie() {

        Map<String, String> maladies = new HashMap<String, String>() {{
            put("MDC", "Maladie débilitante chronique");
            put("FOMO", "Syndrome fear of missing out");
            put("DRS", "Dépendance aux réseaux sociaux");
            put("PEC", "Porphyrie érythropoïétique congénitale");
            put("ZPL", "Zoopathie paraphrénique lycanthropique");
            put("NDMAD", "XXX");
        }};

        List<Map.Entry<String, String>> entries = new ArrayList<>(maladies.entrySet());
        return entries.get(random.nextInt(entries.size()));
    }

    public int genererNiveauMaxAleatoire() {
        return 5 + random.nextInt(5);
    }

    public void augmenterNiveau(){
        if(this.niveauActuel < NIVEAU_MAX){
            this.niveauActuel++;
        }
    }

    public void diminuerNiveau(){
        if(this.niveauActuel > 0){
            this.niveauActuel--;
        }
    }

    public void changerNiveau(int nouveauNiveau){
        if(nouveauNiveau < NIVEAU_MAX &&  nouveauNiveau > 0){
            this.niveauActuel = nouveauNiveau;
        }
    }

    public boolean estLethale() {
        return niveauActuel == NIVEAU_MAX;
    }
    // Getters et setters omis pour la clarté

    public String toString() {
        return this.getClass().getSimpleName() +
                " (" + nomAbrege + ") - " +
                nomComplet +
                " [niveau: " +
                niveauActuel +
                "/" +
                NIVEAU_MAX +
                "]";
    }
} 