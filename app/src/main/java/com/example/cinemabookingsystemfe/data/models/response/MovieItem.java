package com.example.cinemabookingsystemfe.data.models.response;

public class MovieItem {
    private int movieId;
    private String title;
    private String description;
    private int durationMinutes;
    private String director;
    private String trailerUrl;
    private String releaseDate; // yyyy-MM-dd
    private String posterUrl;
    private String country;
    private String rating;
    private String genre;

    public MovieItem(int movieId, String title, String description, int durationMinutes, String director,
                     String trailerUrl, String releaseDate, String posterUrl, String country, String rating, String genre) {
        this.movieId = movieId;
        this.title = title;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.director = director;
        this.trailerUrl = trailerUrl;
        this.releaseDate = releaseDate;
        this.posterUrl = posterUrl;
        this.country = country;
        this.rating = rating;
        this.genre = genre;
    }

    public int getMovieId() { return movieId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getDurationMinutes() { return durationMinutes; }
    public String getDirector() { return director; }
    public String getTrailerUrl() { return trailerUrl; }
    public String getReleaseDate() { return releaseDate; }
    public String getPosterUrl() { return posterUrl; }
    public String getCountry() { return country; }
    public String getRating() { return rating; }
    public String getGenre() { return genre; }
}
