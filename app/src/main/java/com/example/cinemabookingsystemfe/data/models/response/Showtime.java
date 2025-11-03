package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Showtime model - Suất chiếu
 */
public class Showtime {
    
    @SerializedName("showtime_id")
    private int showtimeId;
    
    @SerializedName("movie_id")
    private int movieId;
    
    @SerializedName("cinema_id")
    private int cinemaId;
    
    @SerializedName("auditorium_id")
    private int auditoriumId;
    
    @SerializedName("auditorium_name")
    private String auditoriumName;
    
    @SerializedName("start_time")
    private String startTime; // "2025-11-01 19:30:00"
    
    @SerializedName("end_time")
    private String endTime;
    
    @SerializedName("date")
    private String date; // "2025-11-01"
    
    @SerializedName("time")
    private String time; // "19:30"
    
    @SerializedName("format")
    private String format; // "2D", "3D", "IMAX"
    
    @SerializedName("language")
    private String language; // "Phụ đề", "Lồng tiếng"
    
    @SerializedName("price")
    private double price;
    
    @SerializedName("available_seats")
    private int availableSeats;
    
    @SerializedName("total_seats")
    private int totalSeats;
    
    @SerializedName("status")
    private String status; // "Available", "AlmostFull", "Full"
    
    // Constructors
    public Showtime() {}
    
    public Showtime(int showtimeId, int movieId, int cinemaId, String startTime, String format, double price) {
        this.showtimeId = showtimeId;
        this.movieId = movieId;
        this.cinemaId = cinemaId;
        this.startTime = startTime;
        this.format = format;
        this.price = price;
    }
    
    // Getters and Setters
    public int getShowtimeId() {
        return showtimeId;
    }
    
    public void setShowtimeId(int showtimeId) {
        this.showtimeId = showtimeId;
    }
    
    public int getMovieId() {
        return movieId;
    }
    
    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
    
    public int getCinemaId() {
        return cinemaId;
    }
    
    public void setCinemaId(int cinemaId) {
        this.cinemaId = cinemaId;
    }
    
    public int getAuditoriumId() {
        return auditoriumId;
    }
    
    public void setAuditoriumId(int auditoriumId) {
        this.auditoriumId = auditoriumId;
    }
    
    public String getAuditoriumName() {
        return auditoriumName;
    }
    
    public void setAuditoriumName(String auditoriumName) {
        this.auditoriumName = auditoriumName;
    }
    
    public String getStartTime() {
        return startTime;
    }
    
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    
    public String getEndTime() {
        return endTime;
    }
    
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
    
    public String getFormat() {
        return format;
    }
    
    public void setFormat(String format) {
        this.format = format;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public int getAvailableSeats() {
        return availableSeats;
    }
    
    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
    
    public int getTotalSeats() {
        return totalSeats;
    }
    
    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getDisplayTime() {
        // "19:30" -> "19:30"
        return time != null ? time : startTime.substring(11, 16);
    }
    
    public String getDisplayFormat() {
        // "2D PHỤ ĐỀ"
        return format + " " + (language != null ? language.toUpperCase() : "");
    }
}
