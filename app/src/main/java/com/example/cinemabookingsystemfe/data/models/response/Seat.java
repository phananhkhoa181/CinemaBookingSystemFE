package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Seat model - Ghế ngồi
 * Theo docs: Grid 10 columns x 8 rows (A-H)
 */
public class Seat {
    
    @SerializedName("seatid")
    private int seatId;
    
    @SerializedName("auditoriumid")
    private int auditoriumId;
    
    @SerializedName("row")
    private String row; // "A", "B", "C"...
    
    @SerializedName("number")
    private int number; // Column number 1, 2, 3...
    
    @SerializedName("seattype")
    private String type; // "Standard", "VIP" (no Couple)
    
    @SerializedName("isAvailableForShowtime")
    private boolean isAvailable; // true = available, false = occupied
    
    // Client-side computed fields
    private String seatNumber; // Computed: row + number (e.g., "A1", "B5")
    private int column; // Same as number
    private String status; // Computed: "AVAILABLE" or "OCCUPIED"
    private double price; // From Showtime API
    
    // Client-side state
    private boolean isSelected = false;
    
    // Constructors
    public Seat() {}
    
    public Seat(int seatId, String row, int number, String type, boolean isAvailable, double price) {
        this.seatId = seatId;
        this.row = row;
        this.number = number;
        this.column = number; // Same as number
        this.type = type;
        this.isAvailable = isAvailable;
        this.price = price;
        
        // Compute derived fields
        this.seatNumber = row + number; // e.g., "A1", "B5"
        this.status = isAvailable ? "AVAILABLE" : "OCCUPIED";
    }
    
    // Helper method to initialize computed fields after JSON deserialization
    public void initializeComputedFields() {
        this.seatNumber = row + number;
        this.column = number;
        this.status = isAvailable ? "AVAILABLE" : "OCCUPIED";
    }
    
    // Getters and Setters
    public int getSeatId() {
        return seatId;
    }
    
    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }
    
    public int getAuditoriumId() {
        return auditoriumId;
    }
    
    public void setAuditoriumId(int auditoriumId) {
        this.auditoriumId = auditoriumId;
    }
    
    public String getSeatNumber() {
        return seatNumber;
    }
    
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
    
    public String getRow() {
        return row;
    }
    
    public void setRow(String row) {
        this.row = row;
    }
    
    public int getNumber() {
        return number;
    }
    
    public void setNumber(int number) {
        this.number = number;
        this.column = number;
        this.seatNumber = row + number;
    }
    
    public int getColumn() {
        return column;
    }
    
    public void setColumn(int column) {
        this.column = column;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public boolean getIsAvailable() {
        return isAvailable;
    }
    
    public void setIsAvailable(boolean available) {
        this.isAvailable = available;
        this.status = available ? "AVAILABLE" : "OCCUPIED";
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public boolean isSelected() {
        return isSelected;
    }
    
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public boolean isOccupied() {
        return !isAvailable;
    }
    
    public boolean isVIP() {
        return "VIP".equalsIgnoreCase(type);
    }
}
