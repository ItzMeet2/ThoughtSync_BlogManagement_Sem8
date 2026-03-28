package com.mycompany.blogmanagement.util;

import java.util.regex.Pattern;

public class ValidationUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );
    
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidString(String str, int minLength, int maxLength) {
        return str != null && str.trim().length() >= minLength && str.trim().length() <= maxLength;
    }
    
    public static String sanitizeHtml(String input) {
        if (input == null) return null;
        return input.replaceAll("<", "&lt;")
                   .replaceAll(">", "&gt;")
                   .replaceAll("\"", "&quot;")
                   .replaceAll("'", "&#x27;")
                   .replaceAll("&", "&amp;");
    }
    
    public static boolean isValidInteger(String str) {
        if (str == null || str.trim().isEmpty()) return false;
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}