package com.example.cinemabookingsystemfe.data.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * UpdateUserRequest - Request body cho PUT /api/users/{id}
 * Cập nhật thông tin user (fullname, phone)
 */
public class UpdateUserRequest {
    
    @SerializedName("fullname")
    private String fullName;
    
    @SerializedName("phone")
    private String phone;
    
    // Constructor
    public UpdateUserRequest(String fullName, String phone) {
        this.fullName = fullName;
        this.phone = phone;
    }
    
    // Getters and Setters
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
