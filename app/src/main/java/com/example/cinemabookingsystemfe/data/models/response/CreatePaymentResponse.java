package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;

public class CreatePaymentResponse {
    
    @SerializedName("paymentid")
    private int paymentId;
    
    @SerializedName("bookingid")
    private int bookingId;
    
    @SerializedName("amount")
    private double amount;
    
    @SerializedName("vnpayUrl")
    private String vnpayUrl;
    
    @SerializedName("transactioncode")
    private String transactionCode;
    
    public int getPaymentId() {
        return paymentId;
    }
    
    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }
    
    public int getBookingId() {
        return bookingId;
    }
    
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public String getVnpayUrl() {
        return vnpayUrl;
    }
    
    public void setVnpayUrl(String vnpayUrl) {
        this.vnpayUrl = vnpayUrl;
    }
    
    public String getTransactionCode() {
        return transactionCode;
    }
    
    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }
}
