package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * RefreshTokenResponse - New tokens after refresh
 * Backend returns: token, refreshToken, expiresAt
 */
public class RefreshTokenResponse {
    
    @SerializedName("token")
    private String token;
    
    @SerializedName("refreshToken")
    private String refreshToken;
    
    @SerializedName("expiresAt")
    private String expiresAt;
    
    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    
    public String getExpiresAt() { return expiresAt; }
    public void setExpiresAt(String expiresAt) { this.expiresAt = expiresAt; }
}
