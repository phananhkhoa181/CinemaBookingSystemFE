package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * VerifyOtpResponse - Response from verify-otp endpoint
 */
public class VerifyOtpResponse {
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("isVerified")
    private boolean isVerified;
    
    @SerializedName("verifiedAt")
    private String verifiedAt;
    
    @SerializedName("message")
    private String message;
    
    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
    
    public String getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(String verifiedAt) { this.verifiedAt = verifiedAt; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
