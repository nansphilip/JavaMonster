package com.fantasyhospital.model.maladie;

import java.util.*;

public class Maladie {
    protected String nomComplet;
    protected String nomAbrege;
    protected final int NIVEAU_MAX;
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
    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getNomAbrege() {
        return nomAbrege;
    }

    public void setNomAbrege(String nomAbrege) {
        this.nomAbrege = nomAbrege;
    }

    public int getNIVEAU_MAX() {
        return NIVEAU_MAX;
    }

//    public void setNIVEAU_MAX(int NIVEAU_MAX) {
//        this.NIVEAU_MAX = NIVEAU_MAX;
//    }

    public int getNiveauActuel() {
        return niveauActuel;
    }

    public void setNiveauActuel(int niveauActuel) {
        this.niveauActuel = niveauActuel;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Maladie maladie = (Maladie) o;
        return Objects.equals(nomComplet, maladie.nomComplet) && Objects.equals(nomAbrege, maladie.nomAbrege);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomComplet, nomAbrege);
    }

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