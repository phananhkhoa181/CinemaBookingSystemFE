package com.example.cinemabookingsystemfe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPrefsManager - Quản lý SharedPreferences (JWT token, user info)
 * Singleton Pattern
 */
public class SharedPrefsManager {
    
    private static SharedPrefsManager instance;
    private final SharedPreferences prefs;
    
    private SharedPrefsManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    public static synchronized SharedPrefsManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefsManager(context);
        }
        return instance;
    }
    
    // Token Management
    public void saveToken(String token) {
        prefs.edit().putString(Constants.KEY_TOKEN, token).apply();
    }
    
    public String getToken() {
        return prefs.getString(Constants.KEY_TOKEN, null);
    }
    
    public void saveRefreshToken(String refreshToken) {
        prefs.edit().putString(Constants.KEY_REFRESH_TOKEN, refreshToken).apply();
    }
    
    public String getRefreshToken() {
        return prefs.getString(Constants.KEY_REFRESH_TOKEN, null);
    }
    
    // User Info
    public void saveUserId(int userId) {
        prefs.edit().putInt(Constants.KEY_USER_ID, userId).apply();
    }
    
    public int getUserId() {
        return prefs.getInt(Constants.KEY_USER_ID, -1);
    }
    
    public void saveUserEmail(String email) {
        prefs.edit().putString(Constants.KEY_USER_EMAIL, email).apply();
    }
    
    public String getUserEmail() {
        return prefs.getString(Constants.KEY_USER_EMAIL, null);
    }
    
    public void saveUserName(String name) {
        prefs.edit().putString(Constants.KEY_USER_NAME, name).apply();
    }
    
    public String getUserName() {
        return prefs.getString(Constants.KEY_USER_NAME, null);
    }
    
    // Login Status
    public void setLoggedIn(boolean isLoggedIn) {
        prefs.edit().putBoolean(Constants.KEY_IS_LOGGED_IN, isLoggedIn).apply();
    }
    
    public boolean isLoggedIn() {
        return prefs.getBoolean(Constants.KEY_IS_LOGGED_IN, false);
    }
    
    // Clear All Data (Logout)
    public void clearAll() {
        prefs.edit().clear().apply();
    }
}
