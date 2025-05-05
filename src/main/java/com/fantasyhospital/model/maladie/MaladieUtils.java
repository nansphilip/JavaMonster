package com.fantasyhospital.model.maladie;

import java.util.Random;

import com.fantasyhospital.MaladieType;

public class MaladieUtils {

	protected static final Random RANDOM = new Random();


	public static int genererNiveauMaxAleatoire() {
		return 5 + RANDOM.nextInt(5);
	}

	public static MaladieType getRandomType() {
		MaladieType[] values = MaladieType.values();
		return values[RANDOM.nextInt(values.length)];
	}
}
