package com.example.cinemabookingsystemfe.data.models.request;

import com.google.gson.annotations.SerializedName;

public class CreatePaymentRequest {
    
    @SerializedName("bookingid")
    private int bookingId;
    
    @SerializedName("returnurl")
    private String returnUrl;
    
    public CreatePaymentRequest(int bookingId) {
        this.bookingId = bookingId;
    }
    
    public CreatePaymentRequest(int bookingId, String returnUrl) {
        this.bookingId = bookingId;
        this.returnUrl = returnUrl;
    }
    
    public int getBookingId() {
        return bookingId;
    }
    
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
    
    public String getReturnUrl() {
        return returnUrl;
    }
    
    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }
}
