package com.fantasyhospital.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Enums for female names used in the hospital simulation.
 * When a name is selected, it cannot be used again until it is reset (when a creature or doctor dies)
 * This enum provides a method to get a random name available
 */
@Getter
public enum FemaleNameType {
    GERMAINE, GEORGETTE, YVONNE, PAULETTE, RAYMONDE, LUCIENNE, ANDREE, MARCELLE,
    HENRIETTE, SIMONE, DENISE, SUZANNE, RENEE, FERNANDE, BERTHE, JEANNE, MARGUERITE,
    AUGUSTINE, ALBERTINE, LEONTINE, EMILIENNE, ANTOINETTE, CLEMENTINE, EUGENIE, PHILOMENE,
    ODETTE, COLETTE, HUGUETTE, PIERRETTE, THERESE, JOSEPHINE, AMELIE, IRENE, BERTILLE,
    HORTENSE, EDITH, NOELIE, ARMANDE, HONORINE, CUNEGONDE, ADELINE, BLANCHE, CELINE, DELPHINE,
    ELISE, FLORENCE, GAELLE, HELENE, ISABELLE, JULIETTE, KARINE, LISE, MONIQUE, NADINE,
    OPHELIE, PRISCILLE, ROSALIE, SYLVIE, VICTOIRE, ZOE;

    private boolean selected = false;

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Pick a random name available in the enum
     * @return the name in String format
     */
    public static String getRandomAvailable() {
        List<FemaleNameType> available = new ArrayList<>();
        for (FemaleNameType name : values()) {
            if (!name.isSelected()) {
                available.add(name);
            }
        }

        // If there is no name available, return default name
        if (available.isEmpty()) {
            return "Mickael Martin Nevot";
        }

        FemaleNameType chosen = available.get(new Random().nextInt(available.size()));
        chosen.setSelected(true);

        String nameEnum = chosen.name().toLowerCase();
        return nameEnum.substring(0, 1).toUpperCase() + nameEnum.substring(1);
    }
}
