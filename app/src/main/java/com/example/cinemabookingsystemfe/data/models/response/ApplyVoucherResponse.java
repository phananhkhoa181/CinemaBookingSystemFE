package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Response model for POST /api/bookings/{id}/apply-voucher
 */
public class ApplyVoucherResponse {
    
    @SerializedName("bookingid")
    private int bookingId;
    
    @SerializedName("voucherid")
    private Integer voucherId;
    
    @SerializedName("totalamount")
    private double totalAmount;
    
    @SerializedName("discountamount")
    private double discountAmount;
    
    @SerializedName("status")
    private String status;
    
    // Getters and Setters
    public int getBookingId() {
        return bookingId;
    }
    
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
    
    public Integer getVoucherId() {
        return voucherId;
    }
    
    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public double getDiscountAmount() {
        return discountAmount;
    }
    
    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}
