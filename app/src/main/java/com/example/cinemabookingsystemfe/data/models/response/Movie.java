package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Movie model - Phim (matching API response)
 * Used for GET /api/movies, /api/movies/now-showing, /api/movies/coming-soon, /api/movies/search
 */
public class Movie {
    
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
    
    // Constructors
    public Movie() {}
    
    public Movie(int movieId, String title, String posterUrl, String genre, int durationMinutes, String rating) {
        this.movieId = movieId;
        this.title = title;
        this.posterUrl = posterUrl;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
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
    
    // Helper methods
    public String getDisplayInfo() {
        return genre + " • " + durationMinutes + " phút • " + rating;
    }
    
    /**
     * Get genre as display string (already a string from API)
     */
    public String getGenresString() {
        return genre != null ? genre : "";
    }
    
    /**
     * Get average rating (Note: API doesn't return this in Movie list, only in MovieDetail)
     * Return default value for display
     */
    public double getAverageRating() {
        // Movie list doesn't have averageRating field, only MovieDetail has it
        // Return 0.0 as default, or you can calculate from other data
        return 0.0;
    }
    
    // Backwards compatibility - some old code might use these
    public int getDuration() {
        return durationMinutes;
    }
    
    public String getAgeRating() {
        return rating;
    }
}
