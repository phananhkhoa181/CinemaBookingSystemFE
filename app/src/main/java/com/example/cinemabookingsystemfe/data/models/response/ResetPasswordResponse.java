package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;

public class ResetPasswordResponse {
    @SerializedName("email")
    private String email;

    @SerializedName("message")
    private String message;

    @SerializedName("success")
    private boolean success;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "ResetPasswordResponse{" +
                "email='" + email + '\'' +
                ", message='" + message + '\'' +
                ", success=" + success +
                '}';
    }
}
