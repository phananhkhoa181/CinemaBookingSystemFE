package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * MovieDetail - Chi tiết phim từ API
 * Endpoint: GET /api/movies/{id}
 */
public class MovieDetail {
    
    @SerializedName("movieid")
    private int movieId;
    
    @SerializedName("title")
    private String title;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("durationminutes")
    private int durationMinutes;
    
    @SerializedName("director")
    private String director;
    
    @SerializedName("trailerurl")
    private String trailerUrl;
    
    @SerializedName("releasedate")
    private String releaseDate; // Format: "2012-05-04"
    
    @SerializedName("posterurl")
    private String posterUrl;
    
    @SerializedName("country")
    private String country;
    
    @SerializedName("rating")
    private String rating; // Age rating: "G", "PG", "PG-13", "R", "C18", "P", "T13", "T16"
    
    @SerializedName("genre")
    private String genre; // "Action, Sci-Fi, Adventure"
    
    @SerializedName("averageRating")
    private Double averageRating; // Review rating (1-5 stars)
    
    @SerializedName("totalReviews")
    private int totalReviews;
    
    @SerializedName("totalShowtimes")
    private int totalShowtimes;
    
    // Constructors
    public MovieDetail() {}
    
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
    
    public int getDurationMinutes() {
        return durationMinutes;
    }
    
    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
    
    public String getDirector() {
        return director;
    }
    
    public void setDirector(String director) {
        this.director = director;
    }
    
    public String getTrailerUrl() {
        return trailerUrl;
    }
    
    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }
    
    public String getReleaseDate() {
        return releaseDate;
    }
    
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    
    public String getPosterUrl() {
        return posterUrl;
    }
    
    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getRating() {
        return rating;
    }
    
    public void setRating(String rating) {
        this.rating = rating;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public Double getAverageRating() {
        return averageRating;
    }
    
    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
    
    public int getTotalReviews() {
        return totalReviews;
    }
    
    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }
    
    public int getTotalShowtimes() {
        return totalShowtimes;
    }
    
    public void setTotalShowtimes(int totalShowtimes) {
        this.totalShowtimes = totalShowtimes;
    }
}
