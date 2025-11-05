package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;

public class PaymentDetailResponse {
    
    @SerializedName("paymentid")
    private int paymentId;
    
    @SerializedName("bookingid")
    private int bookingId;
    
    @SerializedName("customerid")
    private int customerId;
    
    @SerializedName("amount")
    private double amount;
    
    @SerializedName("status")
    private String status;
    
    @SerializedName("transactioncode")
    private String transactionCode;
    
    @SerializedName("paymenttime")
    private String paymentTime;
    
    @SerializedName("method")
    private PaymentMethod method;
    
    @SerializedName("booking")
    private BookingSummary booking;
    
    // Nested classes
    public static class PaymentMethod {
        @SerializedName("methodid")
        private int methodId;
        
        @SerializedName("name")
        private String name;
        
        @SerializedName("description")
        private String description;
        
        public int getMethodId() {
            return methodId;
        }
        
        public String getName() {
            return name;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public static class BookingSummary {
        @SerializedName("bookingid")
        private int bookingId;
        
        @SerializedName("bookingcode")
        private String bookingCode;
        
        @SerializedName("status")
        private String status;
        
        @SerializedName("totalamount")
        private double totalAmount;
        
        public int getBookingId() {
            return bookingId;
        }
        
        public String getBookingCode() {
            return bookingCode;
        }
        
        public String getStatus() {
            return status;
        }
        
        public double getTotalAmount() {
            return totalAmount;
        }
    }
    
    // Getters and setters
    public int getPaymentId() {
        return paymentId;
    }
    
    public int getBookingId() {
        return bookingId;
    }
    
    public int getCustomerId() {
        return customerId;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public String getTransactionCode() {
        return transactionCode;
    }
    
    public String getPaymentTime() {
        return paymentTime;
    }
    
    public PaymentMethod getMethod() {
        return method;
    }
    
    public BookingSummary getBooking() {
        return booking;
    }
}
