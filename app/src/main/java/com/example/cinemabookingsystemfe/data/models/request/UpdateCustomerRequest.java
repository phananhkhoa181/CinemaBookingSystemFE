package com.example.cinemabookingsystemfe.data.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * UpdateCustomerRequest - Request body cho PUT /api/customers/profile
 * Cập nhật thông tin customer (address, dateOfBirth, gender)
 */
public class UpdateCustomerRequest {
    
    @SerializedName("address")
    private String address;
    
    @SerializedName("dateofbirth")
    private String dateOfBirth; // Format: "1995-05-15" (yyyy-MM-dd)
    
    @SerializedName("gender")
    private String gender; // "Male", "Female", "Other"
    
    // Constructor
    public UpdateCustomerRequest(String address, String dateOfBirth, String gender) {
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }
    
    // Getters and Setters
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
}
