package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * BookingDetail - Complete booking details from GET /api/bookings/{id}
 */
public class BookingDetail {
    
    @SerializedName("bookingid")
    private int bookingId;
    
    @SerializedName("customerid")
    private int customerId;
    
    @SerializedName("bookingcode")
    private String bookingCode;
    
    @SerializedName("movie")
    private MovieInfo movie;
    
    @SerializedName("cinema")
    private CinemaInfo cinema;
    
    @SerializedName("auditorium")
    private AuditoriumInfo auditorium;
    
    @SerializedName("showtime")
    private ShowtimeInfo showtime;
    
    @SerializedName("seats")
    private List<SeatInfo> seats;
    
    @SerializedName("combos")
    private List<ComboInfo> combos;
    
    @SerializedName("voucher")
    private VoucherInfo voucher;
    
    @SerializedName("payment")
    private PaymentInfo payment;
    
    @SerializedName("totalamount")
    private double totalAmount;
    
    @SerializedName("status")
    private String status;
    
    @SerializedName("bookingtime")
    private String bookingTime;
    
    // Nested classes for related entities
    
    public static class MovieInfo {
        @SerializedName("movieid")
        private int movieId;
        
        @SerializedName("title")
        private String title;
        
        @SerializedName("posterurl")
        private String posterUrl;
        
        @SerializedName("durationminutes")
        private int durationMinutes;
        
        // Getters
        public int getMovieId() { return movieId; }
        public String getTitle() { return title; }
        public String getPosterUrl() { return posterUrl; }
        public int getDurationMinutes() { return durationMinutes; }
    }
    
    public static class CinemaInfo {
        @SerializedName("cinemaid")
        private int cinemaId;
        
        @SerializedName("name")
        private String name;
        
        @SerializedName("address")
        private String address;
        
        @SerializedName("city")
        private String city;
        
        // Getters
        public int getCinemaId() { return cinemaId; }
        public String getName() { return name; }
        public String getAddress() { return address; }
        public String getCity() { return city; }
    }
    
    public static class AuditoriumInfo {
        @SerializedName("auditoriumid")
        private int auditoriumId;
        
        @SerializedName("name")
        private String name;
        
        // Getters
        public int getAuditoriumId() { return auditoriumId; }
        public String getName() { return name; }
    }
    
    public static class ShowtimeInfo {
        @SerializedName("showtimeid")
        private int showtimeId;
        
        @SerializedName("starttime")
        private String startTime;
        
        @SerializedName("endtime")
        private String endTime;
        
        @SerializedName("price")
        private double price;
        
        @SerializedName("format")
        private String format;
        
        @SerializedName("languagetype")
        private String languageType;
        
        // Getters
        public int getShowtimeId() { return showtimeId; }
        public String getStartTime() { return startTime; }
        public String getEndTime() { return endTime; }
        public double getPrice() { return price; }
        public String getFormat() { return format; }
        public String getLanguageType() { return languageType; }
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
        
        // Getters
        public int getSeatId() { return seatId; }
        public String getRow() { return row; }
        public int getNumber() { return number; }
        public String getType() { return type; }
        public double getPrice() { return price; }
    }
    
    public static class ComboInfo {
        @SerializedName("comboid")
        private int comboId;
        
        @SerializedName("name")
        private String name;
        
        @SerializedName("quantity")
        private int quantity;
        
        @SerializedName("price")
        private double price;
        
        // Getters
        public int getComboId() { return comboId; }
        public String getName() { return name; }
        public int getQuantity() { return quantity; }
        public double getPrice() { return price; }
    }
    
    public static class VoucherInfo {
        @SerializedName("voucherid")
        private int voucherId;
        
        @SerializedName("code")
        private String code;
        
        @SerializedName("discounttype")
        private String discountType;
        
        @SerializedName("discountvalue")
        private double discountValue;
        
        // Getters
        public int getVoucherId() { return voucherId; }
        public String getCode() { return code; }
        public String getDiscountType() { return discountType; }
        public double getDiscountValue() { return discountValue; }
    }
    
    public static class PaymentInfo {
        @SerializedName("paymentid")
        private int paymentId;
        
        @SerializedName("amount")
        private double amount;
        
        @SerializedName("status")
        private String status;
        
        @SerializedName("methodname")
        private String methodName;
        
        @SerializedName("transactioncode")
        private String transactionCode;
        
        @SerializedName("paymenttime")
        private String paymentTime;
        
        // Getters
        public int getPaymentId() { return paymentId; }
        public double getAmount() { return amount; }
        public String getStatus() { return status; }
        public String getMethodName() { return methodName; }
        public String getTransactionCode() { return transactionCode; }
        public String getPaymentTime() { return paymentTime; }
    }
    
    // Main getters
    public int getBookingId() { return bookingId; }
    public int getCustomerId() { return customerId; }
    public String getBookingCode() { return bookingCode; }
    public MovieInfo getMovie() { return movie; }
    public CinemaInfo getCinema() { return cinema; }
    public AuditoriumInfo getAuditorium() { return auditorium; }
    public ShowtimeInfo getShowtime() { return showtime; }
    public List<SeatInfo> getSeats() { return seats; }
    public List<ComboInfo> getCombos() { return combos; }
    public VoucherInfo getVoucher() { return voucher; }
    public PaymentInfo getPayment() { return payment; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public String getBookingTime() { return bookingTime; }
}
