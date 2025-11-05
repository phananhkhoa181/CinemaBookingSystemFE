package com.example.cinemabookingsystemfe.data.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Request model for POST /api/bookings/{id}/apply-voucher
 */
public class ApplyVoucherRequest {
    
    @SerializedName("code")
    private String code;
    
    public ApplyVoucherRequest(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
}
