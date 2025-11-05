package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * CustomerProfile - Customer profile từ API
 * Endpoint: GET /api/customers/profile
 */
public class CustomerProfile {
    
    @SerializedName("customerid")
    private int customerId;
    
    @SerializedName("userid")
    private int userId;
    
    @SerializedName("fullname")
    private String fullName;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("phone")
    private String phone;
    
    @SerializedName("address")
    private String address;
    
    @SerializedName("dateofbirth")
    private String dateOfBirth; // Format: "1995-05-15"
    
    @SerializedName("gender")
    private String gender; // "Male", "Female"
    
    @SerializedName("rolename")
    private String roleName;
    
    @SerializedName("createdat")
    private String createdAt; // Format: "2025-10-01T08:00:00"
    
    // Constructors
    public CustomerProfile() {}
    
    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getRoleName() {
        return roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
