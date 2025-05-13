package com.fantasyhospital.enums;

import lombok.Getter;

/**
 * Enum listant les différents types de diseases possibles dans Fantasy
 * Hospital. Chaque disease a un nom complet et un impact sur le moral.
 */
@Getter
public enum DiseaseType {

    /**
     * Debilitating chronic disease
     */
    MDC("Disease débilitante chronique", -5),
    /**
     * Fear of Missing Out Syndrome
     */
    FOMO("Syndrome fear of missing out", -5),
    /**
     * Social media dependency
     */
    DRS("Dépendance aux réseaux sociaux", -5),
    /**
     * Congenital erythropoietic porphyria
     */
    PEC("Porphyrie érythropoïétique congénitale", -5),
    /**
     * Paraphrenic lycanthropic zoopathy
     */
    ZPL("Zoopathie paraphrénique lycanthropique", -5),
    /**
     * Undetermined disease (UDD)
     */
    NDMAD("XXX", -5);

    private final String fullName;
    private final int moralImpact;

    DiseaseType(String fullName, int moralImpact) {
        this.fullName = fullName;
        this.moralImpact = moralImpact;
    }

}
