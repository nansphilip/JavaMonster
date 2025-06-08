package com.fantasyhospital.enums;

public enum GenderType {
	MALE, FEMALE, NON_BINARY;

	public String getLabel() {
		return switch (this) {
			case MALE -> "Homme";
			case FEMALE -> "Femme";
			case NON_BINARY -> "Non-binaire";
		};
	}
}