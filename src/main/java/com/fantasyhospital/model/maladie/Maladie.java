package com.fantasyhospital.model.maladie;

import com.fantasyhospital.enums.MaladieType;
import static com.fantasyhospital.model.maladie.MaladieUtils.genererNiveauMaxAleatoire;
import static com.fantasyhospital.model.maladie.MaladieUtils.getRandomType;

import lombok.Getter;
import lombok.Setter;

public class Maladie {

    protected MaladieType type;

    @Getter
    protected final int NIVEAU_MAX;

    @Setter
    @Getter
    protected int niveauActuel;

    public Maladie() {
        this.type = getRandomType();
        this.NIVEAU_MAX = 10; //genererNiveauMaxAleatoire(); remplacé par 10 (cf. règles)
        this.niveauActuel = 1;
    }

    public Maladie(MaladieType type, int NIVEAU_MAX, int niveauActuel) {
        this.type = type;
        this.NIVEAU_MAX = NIVEAU_MAX;
        this.niveauActuel = niveauActuel;
    }

    public int getImpactMoral() {
        return type.getImpactMoral();
    }

    public String getNom() {
        return type.name();
    }

    public String getNomComplet() {
        return type.getNomComplet();
    }

    public void augmenterNiveau() {
        if (this.niveauActuel < NIVEAU_MAX) {
            this.niveauActuel++;
        }
    }

    public void diminuerNiveau() {
        if (this.niveauActuel > 0) {
            this.niveauActuel--;
        }
    }

    public void changerNiveau(int nouveauNiveau) {
        if (nouveauNiveau < NIVEAU_MAX && nouveauNiveau > 0) {
            this.niveauActuel = nouveauNiveau;
        }
    }

    public boolean estLethale() {
        return niveauActuel == NIVEAU_MAX;
    }

    //    public void setNIVEAU_MAX(int NIVEAU_MAX) {
    //        this.NIVEAU_MAX = NIVEAU_MAX;
    //    }

//    @Override
//    public boolean equals(Object obj) {
//        return super.equals(obj);
//    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Maladie maladie = (Maladie) o;
        return type == maladie.type;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return " " + type
                + " Nom Complet='" + getNomComplet() + '\''
                + ", NIVEAU_MAX=" + NIVEAU_MAX
                + ", niveauActuel=" + niveauActuel;
    }
}
