package com.fantasyhospital.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Enums for the male names used in the game.
 * This enum provides a method to get a random name available
 */
@Getter
public enum MaleNameType {
    LUCIEN, JACQUES, MARCEL, FERNAND, ALBERT, LEON, RAYMOND, GASTON, HENRI, MAURICE,
    ROGER, RENE, ANDRE, GEORGES, PAUL, EMILE, ERNEST, EUGENE, ARMAND, ANATOLE,
    GUSTAVE, ALPHONSE, ACHILLE, AIME, FELIX, THEOPHILE, LEOPOLD, DESIRE, JULES,
    JOSEPH, CLEMENT, BAPTISTE, PHILIBERT, BASILE, CONSTANT, LEONARD, PROSPER, HONORE, OCTAVE,
    ADRIEN, BENOIT, CHARLES, DAMIEN, ETIENNE, FRANCOIS, GILBERT, HUGUES, ISIDORE, JEAN, LAURENT,
    MAXIME, NORBERT, OLIVIER, PATRICE, QUENTIN, RAOUL, SIMON, TIBAUT, VICTOR;

    private boolean selected = false;

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Pick a random name available in the enum
     * @return the name in String format
     */
    public static String getRandomAvailable() {
        List<MaleNameType> available = new ArrayList<>();
        for (MaleNameType name : values()) {
            if (!name.isSelected()) {
                available.add(name);
            }
        }

        // If there is no name available, return default name
        if (available.isEmpty()) {
            return "Mickael Martin Nevot";
        }

        MaleNameType chosen = available.get(new Random().nextInt(available.size()));
        chosen.setSelected(true);

        String nameEnum = chosen.name().toLowerCase();
        return nameEnum.substring(0, 1).toUpperCase() + nameEnum.substring(1);
    }
}
