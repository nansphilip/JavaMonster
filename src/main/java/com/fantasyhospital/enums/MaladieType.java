package com.fantasyhospital.enums;

import lombok.Getter;

/**
 * Enum listant les différents types de maladies possibles dans Fantasy
 * Hospital. Chaque maladie a un nom complet et un impact sur le moral.
 */
@Getter
public enum MaladieType {

    /**
     * Maladie débilitante chronique
     */
    MDC("Maladie débilitante chronique", -5),
    /**
     * Syndrome fear of missing out
     */
    FOMO("Syndrome fear of missing out", -5),
    /**
     * Dépendance aux réseaux sociaux
     */
    DRS("Dépendance aux réseaux sociaux", -5),
    /**
     * Porphyrie érythropoïétique congénitale
     */
    PEC("Porphyrie érythropoïétique congénitale", -5),
    /**
     * Zoopathie paraphrénique lycanthropique
     */
    ZPL("Zoopathie paraphrénique lycanthropique", -5),
    /**
     * Maladie non déterminée (NDMAD)
     */
    NDMAD("XXX", -5);

    private final String nomComplet;
    private final int impactMoral;

    MaladieType(String nomComplet, int impactMoral) {
        this.nomComplet = nomComplet;
        this.impactMoral = impactMoral;
    }

}
