package com.example.cinemabookingsystemfe.data.model;

/**
 * Promotion Model - Khuyến mãi/Banner
 * Maps to backend /api/promotions/active endpoint
 */
public class Promotion {
    private int promotionId;        // Backend: promotionid
    private String name;            // Backend: name
    private String description;     // Backend: description
    private String startDate;       // Backend: startdate
    private String endDate;         // Backend: enddate
    private String discountType;    // Backend: discounttype ("percentage", "fixed")
    private double discountValue;   // Backend: discountvalue
    private String imageUrl;        // For UI only - backend doesn't provide this yet
    
    // Constructors
    public Promotion() {}
    
    public Promotion(int promotionId, String name, String description, 
                    String startDate, String endDate, 
                    String discountType, double discountValue) {
        this.promotionId = promotionId;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountType = discountType;
        this.discountValue = discountValue;
    }
    
    public Promotion(int promotionId, String name, String description, 
                    String startDate, String endDate, 
                    String discountType, double discountValue, String imageUrl) {
        this(promotionId, name, description, startDate, endDate, discountType, discountValue);
        this.imageUrl = imageUrl;
    }
    
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
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    // Helper method để hiển thị discount
    public String getDiscountDisplay() {
        if ("percentage".equalsIgnoreCase(discountType)) {
            return String.format("%.0f%% OFF", discountValue);
        } else {
            return String.format("-%.0fđ", discountValue);
        }
    }
}
