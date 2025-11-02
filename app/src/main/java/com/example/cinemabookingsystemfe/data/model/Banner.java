package com.example.cinemabookingsystemfe.data.model;

public class Banner {
    private int bannerId;
    private String title;
    private String imageUrl;
    private String linkUrl;
    private String targetType; // "Movie", "Promotion", "External"
    private int targetId;
    private boolean isActive;
    
    // Constructors
    public Banner() {
    }
    
    public Banner(int bannerId, String title, String imageUrl, String linkUrl, 
                  String targetType, int targetId, boolean isActive) {
        this.bannerId = bannerId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.targetType = targetType;
        this.targetId = targetId;
        this.isActive = isActive;
    }
    
    // Getters and Setters
    public int getBannerId() {
        return bannerId;
    }
    
    public void setBannerId(int bannerId) {
        this.bannerId = bannerId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getLinkUrl() {
        return linkUrl;
    }
    
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
    
    public String getTargetType() {
        return targetType;
    }
    
    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
    
    public int getTargetId() {
        return targetId;
    }
    
    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
}
