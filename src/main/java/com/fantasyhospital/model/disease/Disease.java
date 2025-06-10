package com.fantasyhospital.model.disease;

import com.fantasyhospital.enums.DiseaseType;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

import static com.fantasyhospital.model.disease.DiseaseUtils.getRandomType;

/**
 * A class representing a disease in the Fantasy Hospital game.
 * It has a type (defined in the enums), a max level (where the disease is lethal) and a current level
 */
@Getter @Setter
public class Disease {

    /**
     * The type of the disease, defined in the enums
     */
    protected DiseaseType type;

    /**
     * The maximum level of the disease, where it becomes lethal
     * This is set to 10, as per the game rules
     */
    protected final int LEVEL_MAX;

    /**
     * The current level of the disease, starts at 1
     * It can be increased or decreased, but cannot exceed LEVEL_MAX
     */
    protected int currentLevel;

    /**
     * Defautl constructor
     * @param type
     * @param LEVEL_MAX
     * @param niveauActuel
     */
    public Disease(DiseaseType type, int LEVEL_MAX, int niveauActuel) {
        this.type = type;
        this.LEVEL_MAX = LEVEL_MAX;
        this.currentLevel = niveauActuel;
    }

    /**
     * Constructor that generates a random disease with a random type
     * the level max attribute is set to 10 and the current level to 1, according to the game rules
     */
    public Disease() {
        this.type = getRandomType();
        this.LEVEL_MAX = 10;
        this.currentLevel = 1;
    }

    /**
     * Returns the name of the disease
     * @return the name of the disease as a String
     */
    public String getName() {
        return type.name();
    }

    /**
     * Returns the full name of the disease
     * @return the full name of the disease as a String
     */
    public String getFullName() {
        return type.getFullName();
    }

    /**
     * Increase the level of a disease by 1, if it is not already at the maximum level.
     */
    public void increaseLevel() {
        if (this.currentLevel < LEVEL_MAX) {
            this.currentLevel++;
        }
    }

    /**
     * Decrease the level of a disease by 1, if it is not already at the minimum level (1).
     */
    public void decreaseLevel() {
        if (this.currentLevel > 0) {
            this.currentLevel--;
        }
    }

    /**
     * Change the level of the disease to a new level, if it is within the valid range (1 to LEVEL_MAX).
     * @param newLevel the new level to set
     */
    public void changeLevel(int newLevel) {
        if (newLevel < LEVEL_MAX && newLevel > 0) {
            this.currentLevel = newLevel;
        }
    }

    /**
     * Checks if the disease is lethal, which is defined as being at the maximum level.
     * @return true if the disease is lethal, false otherwise
     */
    public boolean isLethal() {
        return currentLevel == LEVEL_MAX;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Disease disease)) return false;
        return type == disease.type;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type);
    }

    @Override
    public String toString() {
        return " " + type
                + " Nom Complet='" + getFullName() + '\''
                + ", NIVEAU_MAX=" + LEVEL_MAX
                + ", niveauActuel=" + currentLevel;
    }
}
