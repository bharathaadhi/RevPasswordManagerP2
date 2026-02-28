package com.rev.revpasswordmanagerp2.util;

public class PasswordStrengthUtil {

    public static String checkStrength(String password) {

        if (password == null || password.length() < 6) {
            return "Weak";
        }

        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[@#$%^&+=!].*");

        if (password.length() >= 10 && hasUpper && hasLower && hasDigit && hasSpecial) {
            return "Strong";
        }

        if (password.length() >= 8 && hasUpper && hasLower && hasDigit) {
            return "Medium";
        }

        return "Weak";
    }
}