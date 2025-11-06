package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * AppliedPromotion - Khuyến mãi đã áp dụng cho booking
 */
public class AppliedPromotion implements Serializable {
    
    @SerializedName("promotionid")
    private int promotionId;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("discountapplied")
    private double discountApplied; // Số tiền đã giảm
    
    public AppliedPromotion() {}
    
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
    
    public double getDiscountApplied() {
        return discountApplied;
    }
    
    public void setDiscountApplied(double discountApplied) {
        this.discountApplied = discountApplied;
    }
}
