package com.fantasyhospital.model.creatures.abstractclass;

import java.util.Random;

import com.fantasyhospital.enums.FemaleNameType;
import com.fantasyhospital.enums.GenderType;
import com.fantasyhospital.enums.MaleNameType;

public abstract class BeastUtils {

	protected static final Random RANDOM = new Random();

	public static String generateRandomName(GenderType genderType) {
		return switch (genderType) {
			case MALE:
				yield MaleNameType.getRandomAvailable();
			case FEMALE:
				yield FemaleNameType.getRandomAvailable();
		};
	}

	public static GenderType generateRandomSex() {

		return GenderType.values()[RANDOM.nextInt(GenderType.values().length)];
	}

	public static int generateWeight() {
		return (int)(Math.round((50.0 + RANDOM.nextDouble() * 50.0) * 10.0) / 10.0);
	}

	public static int generateHeight() {
		return (int)((int)Math.round((150.0 + RANDOM.nextDouble() * 50.0) * 10.0) / 10.0);
	}

	public static int generateAge() {
		return 18 + RANDOM.nextInt(60);
	}

	public static int generateMorale() {
		return 60 + RANDOM.nextInt(40);
	}
}
