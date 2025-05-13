package com.fantasyhospital.model.disease;

import java.util.Random;

import com.fantasyhospital.enums.DiseaseType;

public class DiseaseUtils {

    protected static final Random RANDOM = new Random();

    public static int generateRandomDiseaseLevel() {
        return 5 + RANDOM.nextInt(5);
    }

    public static DiseaseType getRandomType() {
        DiseaseType[] values = DiseaseType.values();
        return values[RANDOM.nextInt(values.length)];
    }
}
