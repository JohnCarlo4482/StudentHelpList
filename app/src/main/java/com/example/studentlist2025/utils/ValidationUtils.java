package com.example.studentlist2025.utils;

import android.util.Patterns;

public class ValidationUtils {

    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && Patterns.PHONE.matcher(phone).matches() && phone.length() >= 10;
    }

    public static boolean isValidAmount(String amount) {
        try {
            double value = Double.parseDouble(amount);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isEmptyOrNull(String text) {
        return text == null || text.trim().isEmpty();
    }

    public static String formatCurrency(double amount) {
        return String.format("$%.2f", amount);
    }

    public static String formatPhoneNumber(String phone) {
        if (phone == null || phone.length() < 10) return phone;

        String digits = phone.replaceAll("\\D", "");
        if (digits.length() == 10) {
            return String.format("(%s) %s-%s",
                    digits.substring(0, 3),
                    digits.substring(3, 6),
                    digits.substring(6, 10));
        }
        return phone;
    }
}