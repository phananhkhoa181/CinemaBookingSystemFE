package com.example.cinemabookingsystemfe.utils;

/**
 * Constants - Chứa tất cả các hằng số trong app
 * 
 * TODO: Cập nhật BASE_URL khi có server thật
 */
public class Constants {
    
    // API Configuration
    public static final String BASE_URL = "https://localhost:7001/"; // TODO: Thay bằng URL thật
    public static final String BASE_URL_PROD = "https://api.movie88.com/";
    
    // Mock Mode - Set to true to use mock data (no backend needed)
    public static final boolean USE_MOCK_API = true; // TODO: Set to false when backend is ready
    
    // SharedPreferences Keys
    public static final String PREFS_NAME = "CinemaBookingPrefs";
    public static final String KEY_TOKEN = "jwt_token";
    public static final String KEY_REFRESH_TOKEN = "refresh_token";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";
    
    // Intent Extra Keys
    public static final String EXTRA_MOVIE_ID = "movie_id";
    public static final String EXTRA_SHOWTIME_ID = "showtime_id";
    public static final String EXTRA_BOOKING_ID = "booking_id";
    public static final String EXTRA_PAYMENT_ID = "payment_id";
    public static final String EXTRA_SEAT_IDS = "seat_ids";
    public static final String EXTRA_COMBO_IDS = "combo_ids";
    
    // API Timeouts (seconds)
    public static final int CONNECT_TIMEOUT = 30;
    public static final int READ_TIMEOUT = 30;
    public static final int WRITE_TIMEOUT = 30;
    
    // Seat Lock Duration (milliseconds)
    public static final long SEAT_LOCK_DURATION = 5 * 60 * 1000; // 5 minutes
    
    // Pagination
    public static final int PAGE_SIZE = 10;
    public static final int BANNER_AUTO_SCROLL_DELAY = 3000; // 3 seconds
    
    // Date Formats
    public static final String DATE_FORMAT_API = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_FORMAT_DISPLAY = "dd/MM/yyyy";
    public static final String TIME_FORMAT_DISPLAY = "HH:mm";
    
    // Validation
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_PASSWORD_LENGTH = 50;
    
    // Payment Methods
    public static final String PAYMENT_METHOD_VNPAY = "VNPay";
    
    // Booking Status
    public static final String BOOKING_STATUS_PENDING = "Pending";
    public static final String BOOKING_STATUS_CONFIRMED = "Confirmed";
    public static final String BOOKING_STATUS_COMPLETED = "Completed";
    public static final String BOOKING_STATUS_CANCELLED = "Cancelled";
    
    private Constants() {
        // Private constructor to prevent instantiation
    }
}
