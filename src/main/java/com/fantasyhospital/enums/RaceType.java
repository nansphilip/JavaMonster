package com.fantasyhospital.enums;

import java.util.Random;

/**
 * Enum that represents the diferent races of creatures in the game.
 * Offers a method to generate a random race
 */
public enum RaceType {
    ELF,
    ORC,
    LYCANTHROPE,
    DWARF,
    REPTILIAN,
    VAMPIRE,
    ZOMBIE,
    WEREBEAST;

    /**
     * Generate a random race in the enum
     * @return the race as a String
     */
    public static String generateRandomRace(){
        RaceType raceType = RaceType.values()[new Random().nextInt(RaceType.values().length)];
        String raw = raceType.name().toLowerCase();
        return raw.substring(0, 1).toUpperCase() + raw.substring(1);
    }

    /**
     * Return the race type as String associated with the enum
     * @return
     */
    public String getRace() {
        return switch (this) {
            case ELF -> "Elfe";
            case ORC -> "Orque";
            case LYCANTHROPE -> "Lycantrope";
            case DWARF -> "Nain";
            case REPTILIAN -> "Reptilien";
            case VAMPIRE -> "Vampire";
            case ZOMBIE -> "Zombie";
            case WEREBEAST -> "Homme-bête";
        };
    }
}
