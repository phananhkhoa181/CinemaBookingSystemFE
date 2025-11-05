package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Combo model - Combo bắp nước
 * Used in SelectComboActivity
 * API: GET /api/combos
 */
public class Combo {
    @SerializedName("comboid")
    private int comboId;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("price")
    private double price;
    
    @SerializedName("imageurl")
    private String imageUrl;
    
    // Note: Backend doesn't have isavailable field in entity
    // This is for local UI state only
    private boolean isAvailable;

    // Constructor
    public Combo() {}

    public Combo(int comboId, String name, String description, double price, String imageUrl) {
        this.comboId = comboId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isAvailable = true;
    }

    // Getters and Setters
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
