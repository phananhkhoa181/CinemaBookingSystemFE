package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * ShowtimesByDate - Showtimes grouped by date
 * Endpoint: GET /api/movies/{id}/showtimes
 */
public class ShowtimesByDate {
    
    @SerializedName("date")
    private String date; // Format: "2025-11-05"
    
    @SerializedName("cinemas")
    private List<ShowtimesByCinema> cinemas;
    
    // Constructors
    public ShowtimesByDate() {}
    
    // Getters and Setters
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public List<ShowtimesByCinema> getCinemas() {
        return cinemas;
    }
    
    public void setCinemas(List<ShowtimesByCinema> cinemas) {
        this.cinemas = cinemas;
    }
    
    /**
     * ShowtimesByCinema - Showtimes grouped by cinema
     */
    public static class ShowtimesByCinema {
        
        @SerializedName("cinemaid")
        private int cinemaId;
        
        @SerializedName("name")
        private String name;
        
        @SerializedName("address")
        private String address;
        
        @SerializedName("city")
        private String city;
        
        @SerializedName("latitude")
        private double latitude;
        
        @SerializedName("longitude")
        private double longitude;
        
        @SerializedName("showtimes")
        private List<ShowtimeItem> showtimes;
        
        // Client-side field for calculated distance (not from API)
        private Double distance;
        
        // Getters and Setters
        public int getCinemaId() {
            return cinemaId;
        }
        
        public void setCinemaId(int cinemaId) {
            this.cinemaId = cinemaId;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getAddress() {
            return address;
        }
        
        public void setAddress(String address) {
            this.address = address;
        }
        
        public String getCity() {
            return city;
        }
        
        public void setCity(String city) {
            this.city = city;
        }
        
        public double getLatitude() {
            return latitude;
        }
        
        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }
        
        public double getLongitude() {
            return longitude;
        }
        
        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
        
        public Double getDistance() {
            return distance;
        }
        
        public void setDistance(Double distance) {
            this.distance = distance;
        }
        
        public List<ShowtimeItem> getShowtimes() {
            return showtimes;
        }
        
        public void setShowtimes(List<ShowtimeItem> showtimes) {
            this.showtimes = showtimes;
        }
    }
    
    /**
     * ShowtimeItem - Individual showtime details
     */
    public static class ShowtimeItem {
        
        @SerializedName("showtimeid")
        private int showtimeId;
        
        @SerializedName("starttime")
        private String startTime; // Format: "2025-11-05T10:30:00"
        
        @SerializedName("endtime")
        private String endTime;
        
        @SerializedName("price")
        private double price;
        
        @SerializedName("format")
        private String format; // "2D", "3D", "IMAX"
        
        @SerializedName("languagetype")
        private String languageType; // "Phụ đề", "Lồng tiếng"
        
        @SerializedName("auditoriumid")
        private int auditoriumId; // ID phòng chiếu
        
        @SerializedName("auditoriumName")
        private String auditoriumName; // "Phòng 1", "Phòng 2"
        
        @SerializedName("availableSeats")
        private int availableSeats;
        
        // Getters and Setters
        public int getShowtimeId() {
            return showtimeId;
        }
        
        public void setShowtimeId(int showtimeId) {
            this.showtimeId = showtimeId;
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
        
        public double getPrice() {
            return price;
        }
        
        public void setPrice(double price) {
            this.price = price;
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
        
        public int getAvailableSeats() {
            return availableSeats;
        }
        
        public void setAvailableSeats(int availableSeats) {
            this.availableSeats = availableSeats;
        }
    }
}
