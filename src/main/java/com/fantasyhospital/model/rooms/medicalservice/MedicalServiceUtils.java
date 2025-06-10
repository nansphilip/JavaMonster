package com.fantasyhospital.model.rooms.medicalservice;

import java.util.Random;

import static com.fantasyhospital.enums.ServiceNameType.getRandomAvailable;

/**
 * Utility class for generating random values related to medical services.
 * This class provides methods to generate random budgets, areas, and service names.
 */
public abstract class MedicalServiceUtils {

    //Constants
    public static final int NB_MAX_CREATURES_SERVICE = 5;

    //Fields
    public static Random random = new Random();

    /**
     * Generate a random budget between 40 and 100
     * @return int the budget
     */
    public static int generateRandomBudget(){
        return 40 + random.nextInt(60);
    }

    /**
     * Generate a random area between 20 and 60
     * @return int the area
     */
    public static double generateRandomArea(){
        return 20 + random.nextDouble(40);
    }

    /**
     * Generate a random name in the enum
     * @return String the service name
     */
    public static String generateRandomName() {
        return getRandomAvailable();
    }

}
