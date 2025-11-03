package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Seat model - Ghế ngồi
 * Theo docs: Grid 10 columns x 8 rows (A-H)
 */
public class Seat {
    
    @SerializedName("seat_id")
    private int seatId;
    
    @SerializedName("auditorium_id")
    private int auditoriumId;
    
    @SerializedName("seat_number")
    private String seatNumber; // "A1", "B5", "H10"
    
    @SerializedName("row")
    private String row; // "A", "B", "C"... "H"
    
    @SerializedName("column")
    private int column; // 1-10
    
    @SerializedName("type")
    private String type; // "Standard", "VIP", "Couple"
    
    @SerializedName("status")
    private String status; // "Available", "Occupied", "Maintenance"
    
    @SerializedName("price")
    private double price;
    
    // Client-side state
    private boolean isSelected = false;
    
    // Constructors
    public Seat() {}
    
    public Seat(int seatId, String seatNumber, String row, int column, String type, String status, double price) {
        this.seatId = seatId;
        this.seatNumber = seatNumber;
        this.row = row;
        this.column = column;
        this.type = type;
        this.status = status;
        this.price = price;
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
        return "Available".equalsIgnoreCase(status);
    }
    
    public boolean isOccupied() {
        return "Occupied".equalsIgnoreCase(status);
    }
    
    public boolean isVIP() {
        return "VIP".equalsIgnoreCase(type);
    }
    
    public boolean isCouple() {
        return "Couple".equalsIgnoreCase(type);
    }
}
