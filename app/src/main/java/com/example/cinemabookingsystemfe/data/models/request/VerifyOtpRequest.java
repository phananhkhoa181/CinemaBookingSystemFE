package com.example.cinemabookingsystemfe.data.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * VerifyOtpRequest - Verify OTP code
 * Backend endpoint: POST /api/auth/verify-otp
 */
public class VerifyOtpRequest {
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("otpCode")
    private String otpCode;
    
    @SerializedName("otpType")
    private String otpType;
    
    public VerifyOtpRequest(String email, String otpCode, String otpType) {
        this.email = email;
        this.otpCode = otpCode;
        this.otpType = otpType;
    }
    
    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }
    
    public String getOtpType() { return otpType; }
    public void setOtpType(String otpType) { this.otpType = otpType; }
}
