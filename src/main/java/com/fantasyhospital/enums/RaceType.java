/**
 * Enum représentant les différentes races de créatures présentes dans Fantasy Hospital.
 * Utilisé pour la génération et la gestion des créatures.
 */
package com.fantasyhospital.enums;

import java.util.Random;

public enum RaceType {
    ELF,
    ORC,
    LYCANTHROPE,
    DWARF,
    REPTILIAN,
    VAMPIRE,
    ZOMBIE,
    WEREBEAST;

    public static String generateRandomRace(){
        RaceType raceType = RaceType.values()[new Random().nextInt(RaceType.values().length)];
        String raw = raceType.name().toLowerCase();
        return raw.substring(0, 1).toUpperCase() + raw.substring(1);
    }

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
