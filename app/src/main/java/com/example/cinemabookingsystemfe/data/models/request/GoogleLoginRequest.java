package com.example.cinemabookingsystemfe.data.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * GoogleLoginRequest - Request for POST /api/auth/google-login
 * Send ID Token from Google Sign-In to backend
 */
public class GoogleLoginRequest {
    
    @SerializedName("idToken")
    private String idToken;

    public GoogleLoginRequest(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
