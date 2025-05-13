package com.fantasyhospital.model.disease;

import com.fantasyhospital.enums.DiseaseType;

import static com.fantasyhospital.model.disease.DiseaseUtils.getRandomType;

import lombok.Getter;
import lombok.Setter;

public class Disease {

    protected DiseaseType type;

    @Getter
    protected final int LEVEL_MAX;

    @Setter
    @Getter
    protected int currentLevel;

    public Disease() {
        this.type = getRandomType();
        this.LEVEL_MAX = 10; //genererNiveauMaxAleatoire(); remplacé par 10 (cf. règles)
        this.currentLevel = 1;
    }

    public Disease(DiseaseType type, int LEVEL_MAX, int niveauActuel) {
        this.type = type;
        this.LEVEL_MAX = LEVEL_MAX;
        this.currentLevel = niveauActuel;
    }

    public int getImpactMorale() {
        return type.getMoralImpact();
    }

    public String getName() {
        return type.name();
    }

    public String getFullName() {
        return type.getFullName();
    }

    public void increaseLevel() {
        if (this.currentLevel < LEVEL_MAX) {
            this.currentLevel++;
        }
    }

    public void decreaseLevel() {
        if (this.currentLevel > 0) {
            this.currentLevel--;
        }
    }

    public void changeLevel(int newLevel) {
        if (newLevel < LEVEL_MAX && newLevel > 0) {
            this.currentLevel = newLevel;
        }
    }

    public boolean isLethal() {
        return currentLevel == LEVEL_MAX;
    }

    //    public void setNIVEAU_MAX(int NIVEAU_MAX) {
    //        this.NIVEAU_MAX = NIVEAU_MAX;
    //    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return " " + type
                + " Nom Complet='" + getFullName() + '\''
                + ", NIVEAU_MAX=" + LEVEL_MAX
                + ", niveauActuel=" + currentLevel;
    }
}
