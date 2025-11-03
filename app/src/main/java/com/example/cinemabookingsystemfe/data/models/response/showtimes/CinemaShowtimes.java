package com.example.cinemabookingsystemfe.data.models.response.showtimes;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class CinemaShowtimes implements Serializable {
    @SerializedName("cinemaid")
    private int cinemaId;

    @SerializedName("name")
    private String name;

    @SerializedName("address")
    private String address;

    @SerializedName("city")
    private String city;

    @SerializedName("showtimes")
    private List<Showtime> showtimes;

    public CinemaShowtimes() {}

    public CinemaShowtimes(int cinemaId, String name, String address, String city, List<Showtime> showtimes) {
        this.cinemaId = cinemaId;
        this.name = name;
        this.address = address;
        this.city = city;
        this.showtimes = showtimes;
    }

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

    public List<Showtime> getShowtimes() {
        return showtimes;
    }

    public void setShowtimes(List<Showtime> showtimes) {
        this.showtimes = showtimes;
    }
}
