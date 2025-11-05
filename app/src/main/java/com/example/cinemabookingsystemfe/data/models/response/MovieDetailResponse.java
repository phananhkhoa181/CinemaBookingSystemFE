package com.example.cinemabookingsystemfe.data.models.response;

import java.util.Date;

public class MovieDetailResponse {
    private int movieId;
    private String title;
    private String overview;
    private int duration;
    private String director;
    private String trailerUrl;
    private Date releaseDate;
    private String posterUrl;
    private String country;
    private String ageRating;
    private String genre;
    private double rating;
    private int totalReviews;
    private int totalShowtimes;
    private String backdropUrl;

    // ✅ Getters & setters đầy đủ
    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getTrailerUrl() { return trailerUrl; }
    public void setTrailerUrl(String trailerUrl) { this.trailerUrl = trailerUrl; }

    public Date getReleaseDate() { return releaseDate; }
    public void setReleaseDate(Date releaseDate) { this.releaseDate = releaseDate; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getAgeRating() { return ageRating; }
    public void setAgeRating(String ageRating) { this.ageRating = ageRating; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getTotalReviews() { return totalReviews; }
    public void setTotalReviews(int totalReviews) { this.totalReviews = totalReviews; }

    public int getTotalShowtimes() { return totalShowtimes; }
    public void setTotalShowtimes(int totalShowtimes) { this.totalShowtimes = totalShowtimes; }

    public String getBackdropUrl() { return backdropUrl; }
    public void setBackdropUrl(String backdropUrl) { this.backdropUrl = backdropUrl; }
}
