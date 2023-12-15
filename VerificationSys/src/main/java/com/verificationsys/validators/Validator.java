package com.verificationsys.validators;

public interface Validator<T> {
	boolean validate(T value);
}
