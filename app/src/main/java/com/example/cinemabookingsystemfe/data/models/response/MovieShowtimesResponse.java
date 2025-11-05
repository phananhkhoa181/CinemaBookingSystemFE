package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * MovieShowtimesResponse - Response from GET /api/movies/{id}/showtimes
 * Contains both movie info and showtimes grouped by date
 */
public class MovieShowtimesResponse {
    
    @SerializedName("movie")
    private MovieInfo movie;
    
    @SerializedName("showtimesByDate")
    private List<ShowtimesByDate> showtimesByDate;
    
    // Nested MovieInfo class (simplified movie data from showtimes endpoint)
    public static class MovieInfo {
        @SerializedName("movieid")
        private int movieId;
        
        @SerializedName("title")
        private String title;
        
        @SerializedName("posterurl")
        private String posterUrl;
        
        @SerializedName("durationminutes")
        private int durationMinutes;
        
        @SerializedName("rating")
        private String rating; // Age rating like "PG-13"
        
        // Getters
        public int getMovieId() { return movieId; }
        public String getTitle() { return title; }
        public String getPosterUrl() { return posterUrl; }
        public int getDurationMinutes() { return durationMinutes; }
        public String getRating() { return rating; }
    }
    
    // Getters
    public MovieInfo getMovie() { return movie; }
    public List<ShowtimesByDate> getShowtimesByDate() { return showtimesByDate; }
}
