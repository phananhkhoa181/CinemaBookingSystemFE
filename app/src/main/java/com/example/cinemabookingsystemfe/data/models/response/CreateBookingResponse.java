package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response model for POST /api/bookings/create
 */
public class CreateBookingResponse {
    
    @SerializedName("bookingid")
    private int bookingId;
    
    @SerializedName("bookingcode")
    private String bookingCode; // null until payment confirmed
    
    @SerializedName("customerid")
    private int customerId;
    
    @SerializedName("showtimeid")
    private int showtimeId;
    
    @SerializedName("voucherid")
    private Integer voucherId;
    
    @SerializedName("bookingtime")
    private String bookingTime;
    
    @SerializedName("totalamount")
    private double totalAmount;
    
    @SerializedName("status")
    private String status; // "Pending"
    
    @SerializedName("movie")
    private MovieInfo movie;
    
    @SerializedName("cinema")
    private CinemaInfo cinema;
    
    @SerializedName("showtime")
    private ShowtimeInfo showtime;
    
    @SerializedName("seats")
    private List<SeatInfo> seats;
    
    @SerializedName("appliedPromotions")
    private List<AppliedPromotion> appliedPromotions;
    
    // Nested classes
    public static class MovieInfo {
        @SerializedName("movieid")
        private int movieId;
        
        @SerializedName("title")
        private String title;
        
        @SerializedName("posterurl")
        private String posterUrl;
        
        public int getMovieId() { return movieId; }
        public String getTitle() { return title; }
        public String getPosterUrl() { return posterUrl; }
    }
    
    public static class CinemaInfo {
        @SerializedName("cinemaid")
        private int cinemaId;
        
        @SerializedName("name")
        private String name;
        
        public int getCinemaId() { return cinemaId; }
        public String getName() { return name; }
    }
    
    public static class ShowtimeInfo {
        @SerializedName("starttime")
        private String startTime;
        
        @SerializedName("format")
        private String format;
        
        public String getStartTime() { return startTime; }
        public String getFormat() { return format; }
    }
    
    public static class SeatInfo {
        @SerializedName("seatid")
        private int seatId;
        
        @SerializedName("row")
        private String row;
        
        @SerializedName("number")
        private int number;
        
        @SerializedName("type")
        private String type;
        
        @SerializedName("price")
        private double price;
        
        public int getSeatId() { return seatId; }
        public String getRow() { return row; }
        public int getNumber() { return number; }
        public String getType() { return type; }
        public double getPrice() { return price; }
    }
    
    // Getters
    public int getBookingId() { return bookingId; }
    public String getBookingCode() { return bookingCode; }
    public int getCustomerId() { return customerId; }
    public int getShowtimeId() { return showtimeId; }
    public Integer getVoucherId() { return voucherId; }
    public String getBookingTime() { return bookingTime; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public MovieInfo getMovie() { return movie; }
    public CinemaInfo getCinema() { return cinema; }
    public ShowtimeInfo getShowtime() { return showtime; }
    public List<SeatInfo> getSeats() { return seats; }
    public List<AppliedPromotion> getAppliedPromotions() { return appliedPromotions; }
}
