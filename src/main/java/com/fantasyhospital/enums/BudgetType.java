package com.fantasyhospital.enums;

import lombok.Getter;

@Getter
public enum BudgetType {
    INEXISTANT(0, 0),
    INSUFFISANT(1, 15),
    MEDIOCRE(16, 25),
    FAIBLE(26, 35),
    CORRECT(36, 55),
    BON(56, 75),
    EXCELLENT(75, 100);

    private final int min;
    private final int max;

    BudgetType(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public static BudgetType fromRatio(int ratio) {
        for (BudgetType type : BudgetType.values()) {
            if (ratio >= type.min && ratio <= type.max) {
                return type;
            }
        }
        return null;
    }

    public static BudgetType getRandomBudget() {
        BudgetType[] values = BudgetType.values();
        return values[(int)(Math.random() * values.length)];
    }
}
