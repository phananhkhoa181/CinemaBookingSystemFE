package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response model for GET /api/showtimes/{id}/available-seats
 */
public class AvailableSeatsResponse {
    
    @SerializedName("showtimeid")
    private int showtimeId;
    
    @SerializedName("auditoriumid")
    private int auditoriumId;
    
    @SerializedName("totalSeats")
    private int totalSeats;
    
    @SerializedName("availableSeats")
    private int availableSeats;
    
    @SerializedName("bookedSeats")
    private int bookedSeats;
    
    @SerializedName("seats")
    private List<Seat> seats;
    
    // Getters
    public int getShowtimeId() {
        return showtimeId;
    }
    
    public int getAuditoriumId() {
        return auditoriumId;
    }
    
    public int getTotalSeats() {
        return totalSeats;
    }
    
    public int getAvailableSeats() {
        return availableSeats;
    }
    
    public int getBookedSeats() {
        return bookedSeats;
    }
    
    public List<Seat> getSeats() {
        return seats;
    }
}
