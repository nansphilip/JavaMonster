package com.fantasyhospital.model.creatures.abstractclass;

import com.fantasyhospital.enums.FemaleNameType;
import com.fantasyhospital.enums.GenderType;
import com.fantasyhospital.enums.MaleNameType;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

/**
 * Utils abstract class that give utils methods associated to the beast class
 */
@Slf4j
public abstract class BeastUtils {

	protected static final Random RANDOM = new Random();

	public static void setNameAvailableAgain(Creature creature){
		String name = creature.getFullName();
		if(Objects.equals(name, "Mickael Martin Nevot")) {
			// This is a special case, the name is reserved for the inspirator of the project
			return;
		}

		//Cas particulier des non binaires, on essaie de remettre le prenom de l'enum pour les male puis female
		try {
			MaleNameType enumMaleName = MaleNameType.valueOf(name.toUpperCase());
			enumMaleName.setSelected(false);
			return;
		} catch (IllegalArgumentException ignored) {
		}

		try {
			FemaleNameType enumName = FemaleNameType.valueOf(name.toUpperCase());
			enumName.setSelected(false);
			return;
		} catch (IllegalArgumentException ignored) {
		}

		switch (creature.getSex()){
			case FEMALE:
				FemaleNameType enumName = FemaleNameType.valueOf(name.toUpperCase());
				enumName.setSelected(false);
				break;
			case MALE:
				MaleNameType enumMaleName = MaleNameType.valueOf(name.toUpperCase());
				enumMaleName.setSelected(false);
				break;
		}
	}

	/**
	 * Generates a random name
	 * @param genderType the gendertype given which will determine the type name picked
	 * @return the name as a String
	 */
	public static String generateRandomName(GenderType genderType) {
		return switch (genderType) {
			case MALE:
				yield MaleNameType.getRandomAvailable();
			case FEMALE:
				yield FemaleNameType.getRandomAvailable();
			case NON_BINARY:
				if (Math.random() < 0.5) {
					yield MaleNameType.getRandomAvailable();
				} else {
					yield FemaleNameType.getRandomAvailable();
				}
		};
	}

	/**
	 * Reinitializes the names of the enums to not selected
	 */
	public static void reinitializeNames() {
		Arrays.stream(MaleNameType.values()).forEach(name -> name.setSelected(false));
		Arrays.stream(FemaleNameType.values()).forEach(name -> name.setSelected(false));
	}

	/**
	 * Generates a random GenderType
	 * @return the GenderType
	 */
	public static GenderType generateRandomSex() {

		return GenderType.values()[RANDOM.nextInt(GenderType.values().length)];
	}

	/**
	 * Generates a random weight
	 * @return the weight as int
	 */
	public static int generateWeight() {
		return (int)(Math.round((50.0 + RANDOM.nextDouble() * 50.0) * 10.0) / 10.0);
	}

	/**
	 * Generates a random height
	 * @return the height as int
	 */
	public static int generateHeight() {
		return (int)((int)Math.round((150.0 + RANDOM.nextDouble() * 50.0) * 10.0) / 10.0);
	}

	/**
	 * Generates a random age
	 * @return the age as int
	 */
	public static int generateAge() {
		return 18 + RANDOM.nextInt(60);
	}

	/**
	 * Generates a random morale
	 * @return the morale as int
	 */
	public static int generateMorale() {
		return 60 + RANDOM.nextInt(40);
	}
}
