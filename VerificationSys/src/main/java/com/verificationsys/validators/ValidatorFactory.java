package com.verificationsys.validators;

public class ValidatorFactory {
	public static <T> Validator<T> createValidator(Class<T> type) {
		if (Number.class.isAssignableFrom(type)) {
			// If it is a numeric type
			return (Validator<T>) NumericValidator.getInstance();
		} else if (String.class.isAssignableFrom(type)) {
			// If it is a string type
			return (Validator<T>) EnglishAlphabetsValidator.getInstance();
		} else {
			throw new IllegalArgumentException("Unsupported type for validation: " + type);
		}
	}
}