package com.fantasyhospital.model.creatures.abstractclass;

import java.util.Random;

import com.fantasyhospital.enums.GenderType;

public abstract class BeastUtils {

	protected static final String[] MALE_NAME = {
		"Lucien", "Jacques", "Marcel", "Fernand", "Albert", "Léon", "Raymond", "Gaston", "Henri", "Maurice", "Roger", "René", "André", "Georges", "Paul",
		"Émile", "Ernest", "Eugène", "Armand", "Anatole", "Gustave", "Alphonse", "Achille", "Aimé", "Félix", "Théophile", "Léopold", "Désiré", "Jules",
		"Joseph", "Clément", "Baptiste", "Philibert", "Basile", "Constant", "Léonard", "Prosper", "Anatole", "Honoré", "Octave"
	};

	protected static final String[] FEMALE_NAME = {
		"Germaine", "Georgette", "Yvonne", "Paulette", "Raymonde", "Lucienne", "Andrée", "Marcelle", "Henriette", "Simone", "Denise", "Suzanne", "Renée",
		"Fernande", "Berthe", "Jeanne", "Marguerite", "Augustine", "Albertine", "Léontine", "Émilienne", "Antoinette", "Clémentine", "Eugénie", "Philomène",
		"Odette", "Colette", "Huguette", "Pierrette", "Thérèse", "Joséphine", "Amélie", "Irène", "Bertille", "Hortense", "Édith", "Noëlie", "Armande",
		"Honorine", "Cunégonde"
	};

	protected static final Random RANDOM = new Random();

	public static String generateRandomName(GenderType genderType) {
		return switch (genderType) {
			case MALE:
				int i = RANDOM.nextInt(MALE_NAME.length);
				yield MALE_NAME[i];
			case FEMALE:
				i = RANDOM.nextInt(FEMALE_NAME.length);
				yield FEMALE_NAME[i];
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
