package com.example.cinemabookingsystemfe.data.model;

import java.util.List;

public class Booking {
    private int bookingId;
    private String bookingCode;  // For QR code generation
    private int customerId;
    private int showtimeId;
    private MovieInfo movie;
    private CinemaInfo cinema;
    private ShowtimeInfo showtime;
    private List<String> seats;
    private List<ComboInfo> combos;
    private String voucherCode;
    private double totalAmount;
    private String status;
    private String bookingTime;

    public static class MovieInfo {
        private int movieId;
        private String title;
        private String posterUrl;

        public int getMovieId() { return movieId; }
        public void setMovieId(int movieId) { this.movieId = movieId; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getPosterUrl() { return posterUrl; }
        public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
    }

    public static class CinemaInfo {
        private int cinemaId;
        private String name;
        private String address;

        public int getCinemaId() { return cinemaId; }
        public void setCinemaId(int cinemaId) { this.cinemaId = cinemaId; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
    }

    public static class ShowtimeInfo {
        private String startTime;
        private String format;
        private String languageType;

        public String getStartTime() { return startTime; }
        public void setStartTime(String startTime) { this.startTime = startTime; }

        public String getFormat() { return format; }
        public void setFormat(String format) { this.format = format; }

        public String getLanguageType() { return languageType; }
        public void setLanguageType(String languageType) { this.languageType = languageType; }
    }

    public static class ComboInfo {
        private String name;
        private int quantity;
        private double price;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
    }

    public Booking() {}

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public String getBookingCode() { return bookingCode; }
    public void setBookingCode(String bookingCode) { this.bookingCode = bookingCode; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getShowtimeId() { return showtimeId; }
    public void setShowtimeId(int showtimeId) { this.showtimeId = showtimeId; }

    public MovieInfo getMovie() { return movie; }
    public void setMovie(MovieInfo movie) { this.movie = movie; }

    public CinemaInfo getCinema() { return cinema; }
    public void setCinema(CinemaInfo cinema) { this.cinema = cinema; }

    public ShowtimeInfo getShowtime() { return showtime; }
    public void setShowtime(ShowtimeInfo showtime) { this.showtime = showtime; }

    public List<String> getSeats() { return seats; }
    public void setSeats(List<String> seats) { this.seats = seats; }

    public List<ComboInfo> getCombos() { return combos; }
    public void setCombos(List<ComboInfo> combos) { this.combos = combos; }

    public String getVoucherCode() { return voucherCode; }
    public void setVoucherCode(String voucherCode) { this.voucherCode = voucherCode; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getBookingTime() { return bookingTime; }
    public void setBookingTime(String bookingTime) { this.bookingTime = bookingTime; }

    public String getSeatsDisplay() {
        if (seats == null || seats.isEmpty()) return "";
        return String.join(", ", seats);
    }

    public double getCombosTotal() {
        if (combos == null || combos.isEmpty()) return 0.0;
        double total = 0.0;
        for (ComboInfo combo : combos) {
            total += combo.getPrice() * combo.getQuantity();
        }
        return total;
    }
}