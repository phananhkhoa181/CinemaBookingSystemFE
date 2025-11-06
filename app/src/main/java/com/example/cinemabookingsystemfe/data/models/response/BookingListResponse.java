package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * Response model for /api/bookings/my-bookings
 */
public class BookingListResponse implements Serializable {
    @SerializedName("bookingid")
    private int bookingId;

    @SerializedName("customerid")
    private int customerId;

    @SerializedName("showtimeid")
    private int showtimeId;

    @SerializedName("movie")
    private MovieInfo movie;

    @SerializedName("cinema")
    private CinemaInfo cinema;

    @SerializedName("showtime")
    private ShowtimeInfo showtime;

    @SerializedName("seats")
    private List<String> seats;

    @SerializedName("combos")
    private List<ComboInfo> combos;

    @SerializedName("voucherCode")
    private String voucherCode;

    @SerializedName("bookingcode")
    private String bookingCode;

    @SerializedName("totalamount")
    private double totalAmount;

    @SerializedName("status")
    private String status;

    @SerializedName("bookingtime")
    private String bookingTime;

    // Nested classes
    public static class MovieInfo implements Serializable {
        @SerializedName("movieid")
        private int movieId;

        @SerializedName("title")
        private String title;

        @SerializedName("posterurl")
        private String posterUrl;

        public int getMovieId() {
            return movieId;
        }

        public String getTitle() {
            return title;
        }

        public String getPosterUrl() {
            return posterUrl;
        }
    }

    public static class CinemaInfo implements Serializable {
        @SerializedName("cinemaid")
        private int cinemaId;

        @SerializedName("name")
        private String name;

        @SerializedName("address")
        private String address;

        public int getCinemaId() {
            return cinemaId;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }
    }

    public static class ShowtimeInfo implements Serializable {
        @SerializedName("starttime")
        private String startTime;

        @SerializedName("format")
        private String format;

        @SerializedName("languagetype")
        private String languageType;
        
        @SerializedName("auditoriumname")
        private String auditoriumName;

        public String getStartTime() {
            return startTime;
        }

        public String getFormat() {
            return format;
        }

        public String getLanguageType() {
            return languageType;
        }
        
        public String getAuditoriumName() {
            return auditoriumName;
        }
        
        public void setAuditoriumName(String auditoriumName) {
            this.auditoriumName = auditoriumName;
        }
    }

    public static class ComboInfo implements Serializable {
        @SerializedName("name")
        private String name;

        @SerializedName("quantity")
        private int quantity;

        @SerializedName("price")
        private double price;

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }
    }

    // Getters
    public int getBookingId() {
        return bookingId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getShowtimeId() {
        return showtimeId;
    }

    public MovieInfo getMovie() {
        return movie;
    }

    public CinemaInfo getCinema() {
        return cinema;
    }

    public ShowtimeInfo getShowtime() {
        return showtime;
    }

    public List<String> getSeats() {
        return seats;
    }

    public List<ComboInfo> getCombos() {
        return combos;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public String getBookingTime() {
        return bookingTime;
    }
}
