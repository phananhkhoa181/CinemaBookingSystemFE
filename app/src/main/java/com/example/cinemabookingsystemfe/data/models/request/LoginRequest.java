package com.example.cinemabookingsystemfe.data.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * LoginRequest - Matches Railway backend
 * Backend expects: email + password
 */
public class LoginRequest {
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("password")
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
