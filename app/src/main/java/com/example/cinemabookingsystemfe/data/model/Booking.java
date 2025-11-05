package com.example.cinemabookingsystemfe.data.model;

public class Booking {
    private int bookingId;
    private String bookingCode;
    private int movieId;
    private String movieTitle;
    private String moviePosterUrl;
    private int showtimeId;
    private String showtimeDate;
    private String showtimeTime;
    private String cinemaName;
    private String cinemaAddress;
    private String format; // "2D", "3D", "IMAX"
    private String languageType; // "Phụ đề", "Lồng tiếng"
    private String ageRating; // "C18", "P", "T13", "T16"
    private String seats;
    private double totalPrice;
    private String status; // "Pending", "Confirmed", "Completed", "Cancelled"
    
    // Constructors
    public Booking() {
    }
    
    // Getters and Setters
    public int getBookingId() {
        return bookingId;
    }
    
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
    
    public String getBookingCode() {
        return bookingCode;
    }
    
    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }
    
    public int getMovieId() {
        return movieId;
    }
    
    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
    
    public String getMovieTitle() {
        return movieTitle;
    }
    
    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }
    
    public String getMoviePosterUrl() {
        return moviePosterUrl;
    }
    
    public void setMoviePosterUrl(String moviePosterUrl) {
        this.moviePosterUrl = moviePosterUrl;
    }
    
    public int getShowtimeId() {
        return showtimeId;
    }
    
    public void setShowtimeId(int showtimeId) {
        this.showtimeId = showtimeId;
    }
    
    public String getShowtimeDate() {
        return showtimeDate;
    }
    
    public void setShowtimeDate(String showtimeDate) {
        this.showtimeDate = showtimeDate;
    }
    
    public String getShowtimeTime() {
        return showtimeTime;
    }
    
    public void setShowtimeTime(String showtimeTime) {
        this.showtimeTime = showtimeTime;
    }
    
    public String getCinemaName() {
        return cinemaName;
    }
    
    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }
    
    public String getSeats() {
        return seats;
    }
    
    public void setSeats(String seats) {
        this.seats = seats;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getCinemaAddress() {
        return cinemaAddress;
    }
    
    public void setCinemaAddress(String cinemaAddress) {
        this.cinemaAddress = cinemaAddress;
    }
    
    public String getFormat() {
        return format;
    }
    
    public void setFormat(String format) {
        this.format = format;
    }
    
    public String getLanguageType() {
        return languageType;
    }
    
    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }
    
    public String getAgeRating() {
        return ageRating;
    }
    
    public void setAgeRating(String ageRating) {
        this.ageRating = ageRating;
    }
    
    // Helper methods
    public String getShowtimeFormatted() {
        return showtimeTime + " - " + showtimeDate;
    }
    
    public String getSeatsString() {
        return seats != null ? seats : "";
    }
    
    public String getTotalPriceFormatted() {
        return String.format("%,.0f đ", totalPrice);
    }
    
    public String getFormatDisplay() {
        if (format != null && languageType != null) {
            return format + " - " + (languageType.equals("Phụ đề") ? "SUB" : "DUB");
        }
        return format != null ? format : "";
    }
}
