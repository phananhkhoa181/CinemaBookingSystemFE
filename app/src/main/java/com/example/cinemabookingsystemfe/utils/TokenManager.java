package com.example.cinemabookingsystemfe.utils;

import android.content.Context;
import android.util.Base64;

import org.json.JSONObject;

/**
 * TokenManager - Quản lý JWT Token (decode, validate, check expiry)
 * Singleton Pattern
 */
public class TokenManager {
    
    private static TokenManager instance;
    private SharedPrefsManager prefsManager;
    
    private TokenManager(Context context) {
        prefsManager = SharedPrefsManager.getInstance(context);
    }
    
    public static synchronized TokenManager getInstance(Context context) {
        if (instance == null) {
            instance = new TokenManager(context);
        }
        return instance;
    }
    
    public void saveToken(String token) {
        prefsManager.saveToken(token);
    }
    
    public String getToken() {
        return prefsManager.getToken();
    }
    
    public void saveRefreshToken(String refreshToken) {
        prefsManager.saveRefreshToken(refreshToken);
    }
    
    public String getRefreshToken() {
        return prefsManager.getRefreshToken();
    }
    
    /**
     * Check if token is expired
     * @return true if expired or invalid
     */
    public boolean isTokenExpired() {
        String token = getToken();
        if (token == null || token.isEmpty()) {
            return true;
        }
        
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return true;
            }
            
            // Decode payload (middle part)
            String payload = new String(Base64.decode(parts[1], Base64.DEFAULT));
            JSONObject jsonObject = new JSONObject(payload);
            
            // Get expiry time
            long exp = jsonObject.getLong("exp");
            long currentTime = System.currentTimeMillis() / 1000;
            
            return currentTime > exp;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
    
    /**
     * Get user ID from token
     */
    public int getUserIdFromToken() {
        String token = getToken();
        if (token == null || token.isEmpty()) {
            return -1;
        }
        
        try {
            String[] parts = token.split("\\.");
            String payload = new String(Base64.decode(parts[1], Base64.DEFAULT));
            JSONObject jsonObject = new JSONObject(payload);
            return jsonObject.getInt("userId");
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    /**
     * Clear all tokens
     */
    public void clearTokens() {
        prefsManager.clearAll();
    }
}
