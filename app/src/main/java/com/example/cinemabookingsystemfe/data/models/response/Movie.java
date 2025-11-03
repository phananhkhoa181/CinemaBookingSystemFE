package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Movie model - Phim
 */
public class Movie {
    
    @SerializedName("movie_id")
    private int movieId;
    
    @SerializedName("title")
    private String title;
    
    @SerializedName("poster_url")
    private String posterUrl;
    
    @SerializedName("backdrop_url")
    private String backdropUrl;
    
    @SerializedName("genre")
    private String genre; // "Hành động, Phiêu lưu"
    
    @SerializedName("genres")
    private List<String> genres;
    
    @SerializedName("duration")
    private int duration; // minutes
    
    @SerializedName("age_rating")
    private String ageRating; // "T13", "T16", "T18", "P"
    
    @SerializedName("release_date")
    private String releaseDate;
    
    @SerializedName("director")
    private String director;
    
    @SerializedName("cast")
    private String cast;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("trailer_url")
    private String trailerUrl;
    
    @SerializedName("rating")
    private double rating; // 0-10
    
    @SerializedName("status")
    private String status; // "NowShowing", "ComingSoon"
    
    // Constructors
    public Movie() {}
    
    public Movie(int movieId, String title, String posterUrl, String genre, int duration, String ageRating) {
        this.movieId = movieId;
        this.title = title;
        this.posterUrl = posterUrl;
        this.genre = genre;
        this.duration = duration;
        this.ageRating = ageRating;
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
    
    public String getPosterUrl() {
        return posterUrl;
    }
    
    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
    
    public String getBackdropUrl() {
        return backdropUrl;
    }
    
    public void setBackdropUrl(String backdropUrl) {
        this.backdropUrl = backdropUrl;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public List<String> getGenres() {
        return genres;
    }
    
    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public String getAgeRating() {
        return ageRating;
    }
    
    public void setAgeRating(String ageRating) {
        this.ageRating = ageRating;
    }
    
    public String getReleaseDate() {
        return releaseDate;
    }
    
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    
    public String getDirector() {
        return director;
    }
    
    public void setDirector(String director) {
        this.director = director;
    }
    
    public String getCast() {
        return cast;
    }
    
    public void setCast(String cast) {
        this.cast = cast;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getTrailerUrl() {
        return trailerUrl;
    }
    
    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
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
    
    public String getDisplayInfo() {
        return genre + " • " + duration + " phút • " + ageRating;
    }
}
