package com.fantasyhospital.enums;

import lombok.Getter;

/**
 * Enumerates the different types of actions possible in the Fantasy Hospital
 * simulation. Each action has an impact on the morale of either the creatures
 * or the doctors.
 */

@Getter
public enum ActionType {

    /**
     * Loss of morale for the doctor due to depression
     */
    DOCTOR_DEPRESSION(-40),

    /**
     * Doctor's morale increase after treating a creature
     */
    DOCTOR_HEALS(10),

    /**
     * Creature in triage queue, accompanied
     */
    CREATURE_PENDING_TRIAGE_NOT_ALONE(-5),

    /**
     * Creature in triage queue, alone
     */
    CREATURE_PENDING_TRIAGE_ALONE(-10),

    /**
     * IP awaiting for more than 4 turns: morale reaches 0
     */
    CREATURE_AWAITING_VIP(-100),

    /**
     * Healed creature
     */
    CREATURE_TREATED(50),

    /**
     * Dead creature, demoralizing effect
     */
    DEAD_CREATURE_DEMORALIZING_EFFECT(-10);

    private final int moraleVariation;

    ActionType(int moraleVariation) {
        this.moraleVariation = moraleVariation;
    }

}
