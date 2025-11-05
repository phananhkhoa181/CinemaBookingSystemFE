package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response model for GET /api/auditoriums/{id}/seats?showtimeId={showtimeId}
 * Returns auditorium info with seat availability for a specific showtime
 */
public class AuditoriumSeatsResponse {
    
    @SerializedName("auditoriumid")
    private int auditoriumId;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("seatscount")
    private int seatsCount;
    
    @SerializedName("seats")
    private List<Seat> seats;
    
    // Constructors
    public AuditoriumSeatsResponse() {}
    
    public AuditoriumSeatsResponse(int auditoriumId, String name, int seatsCount, List<Seat> seats) {
        this.auditoriumId = auditoriumId;
        this.name = name;
        this.seatsCount = seatsCount;
        this.seats = seats;
    }
    
    // Getters and Setters
    public int getAuditoriumId() {
        return auditoriumId;
    }
    
    public void setAuditoriumId(int auditoriumId) {
        this.auditoriumId = auditoriumId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getSeatsCount() {
        return seatsCount;
    }
    
    public void setSeatsCount(int seatsCount) {
        this.seatsCount = seatsCount;
    }
    
    public List<Seat> getSeats() {
        return seats;
    }
    
    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}
