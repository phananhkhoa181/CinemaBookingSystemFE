package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * SendOtpResponse - Response from send-otp endpoint
 */
public class SendOtpResponse {
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("otpType")
    private String otpType;
    
    @SerializedName("expiresAt")
    private String expiresAt;
    
    @SerializedName("expiresInMinutes")
    private int expiresInMinutes;
    
    @SerializedName("message")
    private String message;
    
    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getOtpType() { return otpType; }
    public void setOtpType(String otpType) { this.otpType = otpType; }
    
    public String getExpiresAt() { return expiresAt; }
    public void setExpiresAt(String expiresAt) { this.expiresAt = expiresAt; }
    
    public int getExpiresInMinutes() { return expiresInMinutes; }
    public void setExpiresInMinutes(int expiresInMinutes) { this.expiresInMinutes = expiresInMinutes; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
