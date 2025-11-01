package com.example.cinemabookingsystemfe.utils;

import android.util.Patterns;

/**
 * ValidationUtils - Validation cho input fields
 */
public class ValidationUtils {
    
    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    
    /**
     * Validate password length
     */
    public static boolean isValidPassword(String password) {
        return password != null 
                && password.length() >= Constants.MIN_PASSWORD_LENGTH 
                && password.length() <= Constants.MAX_PASSWORD_LENGTH;
    }
    
    /**
     * Validate phone number (Vietnamese format)
     * Examples: 0912345678, +84912345678
     */
    public static boolean isValidPhoneNumber(String phone) {
        if (phone == null) return false;
        String regex = "^(\\+84|0)(3|5|7|8|9)[0-9]{8}$";
        return phone.matches(regex);
    }
    
    /**
     * Check if string is empty or null
     */
    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }
    
    /**
     * Get password strength (0-3)
     * 0 = Weak, 1 = Fair, 2 = Good, 3 = Strong
     */
    public static int getPasswordStrength(String password) {
        if (password == null) return 0;
        
        int strength = 0;
        
        if (password.length() >= 8) strength++;
        if (password.matches(".*[a-z].*") && password.matches(".*[A-Z].*")) strength++; // Has both cases
        if (password.matches(".*\\d.*")) strength++; // Has digit
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) strength++; // Has special char
        
        return Math.min(strength, 3);
    }
    
    /**
     * Validate card number (Luhn algorithm)
     */
    public static boolean isValidCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 13 || cardNumber.length() > 19) {
            return false;
        }
        
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
    
    private ValidationUtils() {
        // Private constructor to prevent instantiation
    }
}
