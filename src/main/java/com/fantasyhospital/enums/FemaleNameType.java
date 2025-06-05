package com.fantasyhospital.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum FemaleNameType {
    GERMAINE, GEORGETTE, YVONNE, PAULETTE, RAYMONDE, LUCIENNE, ANDREE, MARCELLE,
    HENRIETTE, SIMONE, DENISE, SUZANNE, RENEE, FERNANDE, BERTHE, JEANNE, MARGUERITE,
    AUGUSTINE, ALBERTINE, LEONTINE, EMILIENNE, ANTOINETTE, CLEMENTINE, EUGENIE, PHILOMENE,
    ODETTE, COLETTE, HUGUETTE, PIERRETTE, THERESE, JOSEPHINE, AMELIE, IRENE, BERTILLE,
    HORTENSE, EDITH, NOELIE, ARMANDE, HONORINE, CUNEGONDE, ADELINE, BLANCHE, CELINE, DELPHINE,
    ELISE, FLORENCE, GAELLE, HELENE, ISABELLE, JULIETTE, KARINE, LISE, MONIQUE, NADINE,
    OPHELIE, PRISCILLE, ROSALIE, SYLVIE, VICTOIRE, ZOE;

    private boolean selected = false;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public static String getRandomAvailable() {
        List<FemaleNameType> available = new ArrayList<>();
        for (FemaleNameType name : values()) {
            if (!name.isSelected()) {
                available.add(name);
            }
        }

        if (available.isEmpty()) {
            throw new IllegalStateException("Tous les prénoms féminins ont été utilisés !");
        }

        FemaleNameType chosen = available.get(new Random().nextInt(available.size()));
        chosen.setSelected(true);

        String nameEnum = chosen.name().toLowerCase();
        return nameEnum.substring(0, 1).toUpperCase() + nameEnum.substring(1);
    }
}
