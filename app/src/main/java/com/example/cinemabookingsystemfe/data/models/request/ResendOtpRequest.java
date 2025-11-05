package com.example.cinemabookingsystemfe.data.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * ResendOtpRequest - Resend OTP to email
 * Backend endpoint: POST /api/auth/resend-otp
 */
public class ResendOtpRequest {
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("otpType")
    private String otpType;
    
    public ResendOtpRequest(String email, String otpType) {
        this.email = email;
        this.otpType = otpType;
    }
    
    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getOtpType() { return otpType; }
    public void setOtpType(String otpType) { this.otpType = otpType; }
}
