package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Promotion model - Khuyến mãi
 */
public class Promotion implements Serializable {
    
    @SerializedName("promotionid")
    private int promotionId;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("startdate")
    private String startDate; // Format: "yyyy-MM-dd"
    
    @SerializedName("enddate")
    private String endDate; // Format: "yyyy-MM-dd"
    
    @SerializedName("discounttype")
    private String discountType; // "Percent" or "Fixed"
    
    @SerializedName("discountvalue")
    private double discountValue; // e.g., 20 for 20% or 50000 for 50,000 VND
    
    public Promotion() {}
    
    // Getters and Setters
    public int getPromotionId() {
        return promotionId;
    }
    
    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
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
    
    public String getStartDate() {
        return startDate;
    }
    
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    
    public String getEndDate() {
        return endDate;
    }
    
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    
    public String getDiscountType() {
        return discountType;
    }
    
    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }
    
    public double getDiscountValue() {
        return discountValue;
    }
    
    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }
    
    /**
     * Get formatted discount text
     */
    public String getDiscountText() {
        if ("Percent".equalsIgnoreCase(discountType)) {
            return String.format("Giảm %.0f%%", discountValue);
        } else {
            return String.format("Giảm %,.0f VND", discountValue);
        }
    }
    
    /**
     * Get short discount text for badge (e.g., "20% OFF")
     */
    public String getDiscountBadgeText() {
        if ("Percent".equalsIgnoreCase(discountType)) {
            return String.format("%.0f%% OFF", discountValue);
        } else {
            return String.format("%,.0fđ OFF", discountValue);
        }
    }
}
