package com.fantasyhospital;

import lombok.Getter;

@Getter public enum MaladieType {
    MDC("Maladie débilitante chronique", -5),
    FOMO("Syndrome fear of missing out", -5),
    DRS("Dépendance aux réseaux sociaux", -5),
    PEC("Porphyrie érythropoïétique congénitale", -5),
    ZPL("Zoopathie paraphrénique lycanthropique", -5),
    NDMAD("XXX", -5);

    private final String nomComplet;
    private final int impactMoral;

    MaladieType(String nomComplet, int impactMoral) {
        this.nomComplet = nomComplet;
        this.impactMoral = impactMoral;
    }

}
