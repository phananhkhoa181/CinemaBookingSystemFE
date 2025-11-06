package com.example.cinemabookingsystemfe.data.models.request;

import com.google.gson.annotations.SerializedName;

public class CreateReviewRequest {
    
    @SerializedName("movieid")
    private int movieId;
    
    @SerializedName("rating")
    private int rating;
    
    @SerializedName("comment")
    private String comment;

    public CreateReviewRequest(int movieId, int rating, String comment) {
        this.movieId = movieId;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and setters
    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
