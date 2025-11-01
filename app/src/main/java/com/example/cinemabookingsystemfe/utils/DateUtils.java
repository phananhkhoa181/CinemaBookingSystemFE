package com.example.cinemabookingsystemfe.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * DateUtils - Utility class for date formatting and parsing
 */
public class DateUtils {
    
    /**
     * Convert API date string to display format
     * @param apiDateString "2024-01-15T14:30:00"
     * @return "15/01/2024"
     */
    public static String formatDateForDisplay(String apiDateString) {
        try {
            SimpleDateFormat apiFormat = new SimpleDateFormat(Constants.DATE_FORMAT_API, Locale.getDefault());
            SimpleDateFormat displayFormat = new SimpleDateFormat(Constants.DATE_FORMAT_DISPLAY, Locale.getDefault());
            Date date = apiFormat.parse(apiDateString);
            return displayFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return apiDateString;
        }
    }
    
    /**
     * Convert API date string to time format
     * @param apiDateString "2024-01-15T14:30:00"
     * @return "14:30"
     */
    public static String formatTimeForDisplay(String apiDateString) {
        try {
            SimpleDateFormat apiFormat = new SimpleDateFormat(Constants.DATE_FORMAT_API, Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat(Constants.TIME_FORMAT_DISPLAY, Locale.getDefault());
            Date date = apiFormat.parse(apiDateString);
            return timeFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return apiDateString;
        }
    }
    
    /**
     * Get current date in API format
     * @return "2024-01-15T14:30:00"
     */
    public static String getCurrentDateForApi() {
        SimpleDateFormat apiFormat = new SimpleDateFormat(Constants.DATE_FORMAT_API, Locale.getDefault());
        return apiFormat.format(new Date());
    }
    
    /**
     * Format milliseconds to MM:SS (for countdown timer)
     * @param millis milliseconds
     * @return "05:00"
     */
    public static String formatCountdownTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds);
    }
    
    private DateUtils() {
        // Private constructor to prevent instantiation
    }
}
