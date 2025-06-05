package com.fantasyhospital.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum MaleNameType {
    LUCIEN, JACQUES, MARCEL, FERNAND, ALBERT, LEON, RAYMOND, GASTON, HENRI, MAURICE,
    ROGER, RENE, ANDRE, GEORGES, PAUL, EMILE, ERNEST, EUGENE, ARMAND, ANATOLE,
    GUSTAVE, ALPHONSE, ACHILLE, AIME, FELIX, THEOPHILE, LEOPOLD, DESIRE, JULES,
    JOSEPH, CLEMENT, BAPTISTE, PHILIBERT, BASILE, CONSTANT, LEONARD, PROSPER, HONORE, OCTAVE,
    ADRIEN, BENOIT, CHARLES, DAMIEN, ETIENNE, FRANCOIS, GILBERT, HUGUES, ISIDORE, JEAN, LAURENT,
    MAXIME, NORBERT, OLIVIER, PATRICE, QUENTIN, RAOUL, SIMON, TIBAUT, VICTOR;

    private boolean selected = false;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public static String getRandomAvailable() {
        List<MaleNameType> available = new ArrayList<>();
        for (MaleNameType name : values()) {
            if (!name.isSelected()) {
                available.add(name);
            }
        }

        if (available.isEmpty()) {
            throw new IllegalStateException("Tous les prénoms masculins ont été utilisés !");
        }

        MaleNameType chosen = available.get(new Random().nextInt(available.size()));
        chosen.setSelected(true);

        String nameEnum = chosen.name().toLowerCase();
        return nameEnum.substring(0, 1).toUpperCase() + nameEnum.substring(1);
    }
}
