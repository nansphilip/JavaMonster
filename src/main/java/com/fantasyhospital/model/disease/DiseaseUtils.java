package com.fantasyhospital.model.disease;

import com.fantasyhospital.enums.DiseaseType;

import java.util.Random;

/**
 * Utility class for generating random disease levels and types.
 * Provides methods to create random disease levels and select random disease types.
 */
public class DiseaseUtils {

    protected static final Random RANDOM = new Random();

    /**
     * Generates a random disease level between 5 and 9.
     *
     * @return an integer representing the disease level
     */
    public static int generateRandomDiseaseLevel() {
        return 5 + RANDOM.nextInt(5);
    }

    /**
     * Picks a random disease type from the DiseaseType enum.
     *
     * @return a randomly selected DiseaseType
     */
    public static DiseaseType getRandomType() {
        DiseaseType[] values = DiseaseType.values();
        return values[RANDOM.nextInt(values.length)];
    }
}
