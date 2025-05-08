package com.fantasyhospital.enums;

import lombok.Getter;

/**
 * Enumère les différents types d'actions possibles dans la simulation Fantasy
 * Hospital. Chaque action a un impact sur le moral des créatures ou des
 * médecins.
 */
@Getter
public enum ActionType {

    /**
     * Perte de moral du médecin suite à une dépression
     */
    MEDECIN_DEPRESSION(-40),

    /**
     * Gain de moral du médecin après avoir soigné une créature
     */
    MEDECIN_SOIGNE(15),

    /**
     * Créature en attente de triage, non seule
     */
    CREATURE_ATTENTE_TRIAGE_NONSEUL(-5),

    /**
     * Créature en attente de triage, seule
     */
    CREATURE_ATTENTE_TRIAGE_SEUL(-10),

    /**
     * VIP en attente plus de 4 tours : moral tombe à 0
     */
    CREATURE_ATTENTE_VIP(-100),

    /**
     * Créature soignée
     */
    CREATURE_SOIN(50),

    /**
     * Créature trépassée, effet démoralisant
     */
    CREATE_TREPASSE_DEMORALISANT(-10);

    private final int variationMoral;

    ActionType(int variationMoral) {
        this.variationMoral = variationMoral;
    }

}
