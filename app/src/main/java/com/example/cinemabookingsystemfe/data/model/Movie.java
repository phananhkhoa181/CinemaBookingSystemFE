package com.example.cinemabookingsystemfe.data.model;

import java.util.List;

public class Movie {
    private int movieId;
    private String title;
    private String description;
    private String posterUrl;
    private String trailerUrl;
    private int duration;
    private String releaseDate;
    private double rating;
    private String status; // "NowShowing", "ComingSoon"
    private List<String> genres;
    
    // Constructors
    public Movie() {
    }
    
    public Movie(int movieId, String title, String posterUrl, double rating) {
        this.movieId = movieId;
        this.title = title;
        this.posterUrl = posterUrl;
        this.rating = rating;
    }
    
    // Getters and Setters
    public int getMovieId() {
        return movieId;
    }
    
    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getPosterUrl() {
        return posterUrl;
    }
    
    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
    
    public String getTrailerUrl() {
        return trailerUrl;
    }
    
    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public String getReleaseDate() {
        return releaseDate;
    }
    
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    
    public double getRating() {
        return rating;
    }
    
    public void setRating(double rating) {
        this.rating = rating;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public List<String> getGenres() {
        return genres;
    }
    
    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
    
    // Helper method
    public String getGenresString() {
        if (genres == null || genres.isEmpty()) {
            return "";
        }
        return String.join(", ", genres);
    }
}
