package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Cinema model - Rạp chiếu phim
 */
public class Cinema {
    
    @SerializedName("cinemaid")
    private int cinemaId;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("address")
    private String address;
    
    @SerializedName("district")
    private String district;
    
    @SerializedName("city")
    private String city;
    
    @SerializedName("phone")
    private String phone;
    
    @SerializedName("latitude")
    private double latitude;
    
    @SerializedName("longitude")
    private double longitude;
    
    @SerializedName("distance")
    private Double distance; // km - calculated from user location
    
    @SerializedName("showtimes")
    private List<Showtime> showtimes;
    
    // Constructors
    public Cinema() {}
    
    public Cinema(int cinemaId, String name, String address, String district, String city) {
        this.cinemaId = cinemaId;
        this.name = name;
        this.address = address;
        this.district = district;
        this.city = city;
    }
    
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
    
    public String getDistrict() {
        return district;
    }
    
    public void setDistrict(String district) {
        this.district = district;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
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
    
    public List<Showtime> getShowtimes() {
        return showtimes;
    }
    
    public void setShowtimes(List<Showtime> showtimes) {
        this.showtimes = showtimes;
    }
    
    public String getFullAddress() {
        return address + ", " + district + ", " + city;
    }
}
