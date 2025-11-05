package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * ReviewResponse - Response from GET /api/Reviews/movie/{movieId}
 */
public class ReviewResponse {
    
    @SerializedName("reviews")
    private List<Review> reviews;
    
    @SerializedName("currentPage")
    private int currentPage;
    
    @SerializedName("totalPages")
    private int totalPages;
    
    @SerializedName("pageSize")
    private int pageSize;
    
    @SerializedName("totalCount")
    private int totalCount;
    
    @SerializedName("hasPrevious")
    private boolean hasPrevious;
    
    @SerializedName("hasNext")
    private boolean hasNext;
    
    @SerializedName("averageRating")
    private double averageRating;
    
    // Getters
    public List<Review> getReviews() { return reviews; }
    public int getCurrentPage() { return currentPage; }
    public int getTotalPages() { return totalPages; }
    public int getPageSize() { return pageSize; }
    public int getTotalCount() { return totalCount; }
    public boolean isHasPrevious() { return hasPrevious; }
    public boolean isHasNext() { return hasNext; }
    public double getAverageRating() { return averageRating; }
    
    /**
     * Review item
     */
    public static class Review {
        @SerializedName("reviewid")
        private int reviewId;
        
        @SerializedName("customerid")
        private int customerId;
        
        @SerializedName("movieid")
        private int movieId;
        
        @SerializedName("rating")
        private int rating;
        
        @SerializedName("comment")
        private String comment;
        
        @SerializedName("createdat")
        private String createdAt;
        
        @SerializedName("customer")
        private Customer customer;
        
        // Getters
        public int getReviewId() { return reviewId; }
        public int getCustomerId() { return customerId; }
        public int getMovieId() { return movieId; }
        public int getRating() { return rating; }
        public String getComment() { return comment; }
        public String getCreatedAt() { return createdAt; }
        public Customer getCustomer() { return customer; }
    }
    
    /**
     * Customer info in review
     */
    public static class Customer {
        @SerializedName("customerid")
        private int customerId;
        
        @SerializedName("fullname")
        private String fullName;
        
        @SerializedName("gender")
        private String gender;
        
        // Getters
        public int getCustomerId() { return customerId; }
        public String getFullName() { return fullName; }
        public String getGender() { return gender; }
    }
}
