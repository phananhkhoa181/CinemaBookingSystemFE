package com.example.cinemabookingsystemfe.data.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * RefreshTokenRequest - Refresh expired JWT token
 * Backend endpoint: POST /api/auth/refresh-token
 */
public class RefreshTokenRequest {
    
    @SerializedName("refreshToken")
    private String refreshToken;
    
    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    // Getters and Setters
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
