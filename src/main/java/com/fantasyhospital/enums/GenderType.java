package com.fantasyhospital.enums;

/**
 * Enum that represents the diferent gender types of creatures in the hospital
 */
public enum GenderType {
	MALE, FEMALE, NON_BINARY;

	/**
	 * Returns the label associated with the gender
	 * @return the label
	 */
	public String getLabel() {
		return switch (this) {
			case MALE -> "Homme";
			case FEMALE -> "Femme";
			case NON_BINARY -> "Non-binaire";
		};
	}
}