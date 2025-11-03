package com.example.cinemabookingsystemfe.data.models.response.showtimes;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class ShowtimesDate implements Serializable {
    @SerializedName("date")
    private String date; // "2025-11-05" - keep as String (ISO date) for now

    @SerializedName("cinemas")
    private List<CinemaShowtimes> cinemas;

    public ShowtimesDate() {}

    public ShowtimesDate(String date, List<CinemaShowtimes> cinemas) {
        this.date = date;
        this.cinemas = cinemas;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<CinemaShowtimes> getCinemas() {
        return cinemas;
    }

    public void setCinemas(List<CinemaShowtimes> cinemas) {
        this.cinemas = cinemas;
    }
}

