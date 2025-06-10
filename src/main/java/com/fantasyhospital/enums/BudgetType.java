package com.fantasyhospital.enums;

import lombok.Getter;

/**
 * Enum for the several Budget types
 */
@Getter
public enum BudgetType {
    INEXISTANT(0, 0),
    INSUFFISANT(1, 15),
    MEDIOCRE(16, 25),
    FAIBLE(26, 35),
    CORRECT(36, 55),
    BON(56, 75),
    EXCELLENT(75, 100);

    /**
     * Bornes for the budget
     */
    private final int min;
    private final int max;

    /**
     * Constructor for the BudgetType enum
     * @param min minimum value of the budget type
     * @param max maximum value of the budget type
     */
    BudgetType(int min, int max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Returns the BudgetType corresponding to the given ratio
     * @param ratio the budget ratio to check
     * @return the BudgetType corresponding to the ratio, or null if no match is found
     */
    public static BudgetType fromRatio(int ratio) {
        for (BudgetType type : BudgetType.values()) {
            if (ratio >= type.min && ratio <= type.max) {
                return type;
            }
        }
        return null;
    }
}
