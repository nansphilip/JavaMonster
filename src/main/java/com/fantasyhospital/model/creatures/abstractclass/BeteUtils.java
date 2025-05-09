package com.fantasyhospital.model.creatures.abstractclass;

import java.util.Random;

import com.fantasyhospital.enums.GenderType;

public abstract class BeteUtils {

	protected static final String[] NOM_MASCULIN = {
		"Lucien", "Jacques", "Marcel", "Fernand", "Albert", "Léon", "Raymond", "Gaston", "Henri", "Maurice", "Roger", "René", "André", "Georges", "Paul",
		"Émile", "Ernest", "Eugène", "Armand", "Anatole", "Gustave", "Alphonse", "Achille", "Aimé", "Félix", "Théophile", "Léopold", "Désiré", "Jules",
		"Joseph", "Clément", "Baptiste", "Philibert", "Basile", "Constant", "Léonard", "Prosper", "Anatole", "Honoré", "Octave"
	};

	protected static final String[] NOM_FEMININ = {
		"Germaine", "Georgette", "Yvonne", "Paulette", "Raymonde", "Lucienne", "Andrée", "Marcelle", "Henriette", "Simone", "Denise", "Suzanne", "Renée",
		"Fernande", "Berthe", "Jeanne", "Marguerite", "Augustine", "Albertine", "Léontine", "Émilienne", "Antoinette", "Clémentine", "Eugénie", "Philomène",
		"Odette", "Colette", "Huguette", "Pierrette", "Thérèse", "Joséphine", "Amélie", "Irène", "Bertille", "Hortense", "Édith", "Noëlie", "Armande",
		"Honorine", "Cunégonde"
	};

	protected static final Random RANDOM = new Random();

	public static String genererNomAleatoire() {
		//        String[] prefix = {"Kra", "Zor", "El", "Thra", "Gor", "Vel", "Mor", "Sha", "Lun", "Dra"};
		//        String[] suffix = {"gor", "nax", "iel", "dor", "vak", "mir", "thar", "dil", "rak", "zul"};
		//
		//        return prefix[random.nextInt(prefix.length)] + suffix[random.nextInt(suffix.length)];
		GenderType sexe = GenderType.MALE;
		return switch (sexe) {
			case MALE:
				int i = RANDOM.nextInt(NOM_MASCULIN.length);
				yield NOM_MASCULIN[i];
			case FEMALE:
				i = RANDOM.nextInt(NOM_FEMININ.length);
				yield NOM_FEMININ[i];
		};
	}

	public static GenderType genererSexeAleatoire() {

		return GenderType.values()[RANDOM.nextInt(GenderType.values().length)];
	}

	public static int genererPoids() {
		return (int)(Math.round((50.0 + RANDOM.nextDouble() * 50.0) * 10.0) / 10.0);
	}

	public static int genererTaille() {
		return (int)((int)Math.round((150.0 + RANDOM.nextDouble() * 50.0) * 10.0) / 10.0);
	}

	public static int genererAge() {
		return 18 + RANDOM.nextInt(60);
	}

	public static int genererMoral() {
		return 60 + RANDOM.nextInt(40);
	}
}
