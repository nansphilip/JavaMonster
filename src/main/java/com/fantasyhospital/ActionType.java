package com.fantasyhospital;

import lombok.Getter;

@Getter public enum ActionType {
    // MEDECINS
    MEDECIN_DEPRESSION(-40),
    MEDECIN_SOIGNE(15),
    // MONSTRES

    // TRIAGE
    CREATURE_ATTENTE_TRIAGE_NONSEUL(-5),
    CREATURE_ATTENTE_TRIAGE_SEUL(-10),

    // VIP APRES 4 TOURS MORAL DIRECTEMENT 0
    CREATURE_ATTENTE_VIP(-100),

    // ETRE SOIGNER
    CREATURE_SOIN(50),

    // TREPASSE - DEMORALISANT
    CREATE_TREPASSE_DEMORALISANT(-10);


    private final int variationMoral;

    ActionType(int variationMoral) {
        this.variationMoral = variationMoral;
    }

}
