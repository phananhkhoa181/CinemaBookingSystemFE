package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * RegisterResponse - Response from POST /api/auth/register
 * Matches Railway backend response format
 * Backend returns: email, fullName, message, otpSent, otpExpiresAt
 */
public class RegisterResponse {
    
    @SerializedName("userId")
    private int userId;
    
    @SerializedName("fullname")
    private String fullname;
    
    @SerializedName("fullName")  // Backend uses camelCase
    private String fullName;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("phone")
    private String phone;
    
    @SerializedName("roleName")
    private String roleName;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("otpSent")
    private boolean otpSent;
    
    @SerializedName("otpExpiresAt")
    private String otpExpiresAt;

    public RegisterResponse() {}

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFullname() { 
        // Try fullName first (backend format), fallback to fullname
        return fullName != null ? fullName : fullname; 
    }
    public void setFullname(String fullname) { this.fullname = fullname; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public boolean isOtpSent() { return otpSent; }
    public void setOtpSent(boolean otpSent) { this.otpSent = otpSent; }
    
    public String getOtpExpiresAt() { return otpExpiresAt; }
    public void setOtpExpiresAt(String otpExpiresAt) { this.otpExpiresAt = otpExpiresAt; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
}
