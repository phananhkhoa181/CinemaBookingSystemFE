package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response model for POST /api/bookings/{id}/add-combos
 */
public class AddCombosResponse {
    
    @SerializedName("bookingid")
    private int bookingId;
    
    @SerializedName("combos")
    private List<ComboDetail> combos;
    
    // Try both camelCase and lowercase for combo total
    @SerializedName(value = "comboTotal", alternate = {"combototal"})
    private double comboTotal;
    
    @SerializedName(value = "seatsTotal", alternate = {"seatstotal"})
    private double seatsTotal;
    
    @SerializedName("totalamount")
    private double totalAmount;
    
    // Getters and Setters
    public int getBookingId() {
        return bookingId;
    }
    
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
    
    public List<ComboDetail> getCombos() {
        return combos;
    }
    
    public void setCombos(List<ComboDetail> combos) {
        this.combos = combos;
    }
    
    public double getComboTotal() {
        return comboTotal;
    }
    
    public void setComboTotal(double comboTotal) {
        this.comboTotal = comboTotal;
    }
    
    public double getSeatsTotal() {
        return seatsTotal;
    }
    
    public void setSeatsTotal(double seatsTotal) {
        this.seatsTotal = seatsTotal;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    /**
     * Nested class for combo detail in response
     */
    public static class ComboDetail {
        @SerializedName("comboid")
        private int comboId;
        
        @SerializedName("name")
        private String name;
        
        @SerializedName("quantity")
        private int quantity;
        
        @SerializedName("price")
        private double price; // Total price (unit price × quantity)
        
        public int getComboId() {
            return comboId;
        }
        
        public void setComboId(int comboId) {
            this.comboId = comboId;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public int getQuantity() {
            return quantity;
        }
        
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
        
        public double getPrice() {
            return price;
        }
        
        public void setPrice(double price) {
            this.price = price;
        }
    }
}
