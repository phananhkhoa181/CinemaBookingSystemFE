package com.example.cinemabookingsystemfe.data.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * RegisterRequest - Matches Railway backend
 * Backend ACTUALLY expects: fullName, email, password, confirmPassword, phoneNumber
 * (Backend docs are incorrect - use phoneNumber not phone!)
 */
public class RegisterRequest {
    
    @SerializedName("fullName")
    private String fullname;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("password")
    private String password;
    
    @SerializedName("confirmPassword")
    private String confirmPassword;
    
    @SerializedName("phoneNumber")
    private String phone;

    public RegisterRequest(String fullname, String email, String password, String confirmPassword, String phone) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.phone = phone;
    }

    // Getters and Setters
    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    @Override
    public String toString() {
        return "RegisterRequest{" +
                "fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + (password != null ? "***" : "null") + '\'' +
                ", confirmPassword='" + (confirmPassword != null ? "***" : "null") + '\'' +
                '}';
    }
}
