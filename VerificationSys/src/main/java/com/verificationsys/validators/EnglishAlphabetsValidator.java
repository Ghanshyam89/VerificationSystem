package com.verificationsys.validators;

public class EnglishAlphabetsValidator implements Validator<String> {
	private static final EnglishAlphabetsValidator instance = new EnglishAlphabetsValidator();

	private EnglishAlphabetsValidator() {
		// Private constructor to enforce singleton pattern
	}

	public static EnglishAlphabetsValidator getInstance() {
		return instance;
	}

	@Override
	public boolean validate(String value) {
		// English alphabets validation logic
		return value != null && value.matches("^[a-zA-Z]+$"); // Example: Check if the string contains only English
															  // alphabets
	}
}