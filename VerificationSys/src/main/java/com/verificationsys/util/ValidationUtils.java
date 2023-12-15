package com.verificationsys.util;

public class ValidationUtils {
    public static boolean isParsableToInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}