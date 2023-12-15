package com.verificationsys.validators;

public class NumericValidator implements Validator<Number> {
	private static final NumericValidator instance = new NumericValidator();

	private NumericValidator() {
		// Private constructor to enforce singleton pattern
	}

	public static NumericValidator getInstance() {
		return instance;
	}

	@Override
	public boolean validate(Number value) {
		// Numeric validation logic
		return value != null && value.intValue() >= 0; // Example: Check if the number is non-negative
	}
}