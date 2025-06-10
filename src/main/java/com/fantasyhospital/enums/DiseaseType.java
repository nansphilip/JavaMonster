package com.fantasyhospital.enums;

import lombok.Getter;

/**
 * Enum of the diferents disease types in the Fantasy
 * Hospital. Each disease has a full name and a moral impact
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
    NDMAD("Neo Diarrhée Monolithique Avec Débit", -5);

    /**
     * Full name of the disease
     */
    private final String fullName;

    /**
     * Moral impact of the disease
     */
    private final int moralImpact;

    DiseaseType(String fullName, int moralImpact) {
        this.fullName = fullName;
        this.moralImpact = moralImpact;
    }
}
