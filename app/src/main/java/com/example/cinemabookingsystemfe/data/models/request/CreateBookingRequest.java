package com.example.cinemabookingsystemfe.data.models.request;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Request model for POST /api/bookings/create
 */
public class CreateBookingRequest {
    
    @SerializedName("showtimeid")
    private int showtimeId;
    
    @SerializedName("seatids")
    private List<Integer> seatIds;
    
    public CreateBookingRequest(int showtimeId, List<Integer> seatIds) {
        this.showtimeId = showtimeId;
        this.seatIds = seatIds;
    }
    
    public int getShowtimeId() {
        return showtimeId;
    }
    
    public void setShowtimeId(int showtimeId) {
        this.showtimeId = showtimeId;
    }
    
    public List<Integer> getSeatIds() {
        return seatIds;
    }
    
    public void setSeatIds(List<Integer> seatIds) {
        this.seatIds = seatIds;
    }
}
