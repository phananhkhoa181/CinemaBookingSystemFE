package com.example.cinemabookingsystemfe.data.models.response.showtimes;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Showtime implements Serializable {
    @SerializedName("showtimeid")
    private int showtimeId;

    // ISO datetime strings, parse later if needed
    @SerializedName("starttime")
    private String startTime;

    @SerializedName("endtime")
    private String endTime;

    @SerializedName("price")
    private int price;

    @SerializedName("format")
    private String format;

    @SerializedName("languagetype")
    private String languageType;

    @SerializedName("auditorium")
    private Auditorium auditorium;

    @SerializedName("availableSeats")
    private int availableSeats;

    public Showtime() {}

    public Showtime(int showtimeId, String startTime, String endTime, int price,
                    String format, String languageType, Auditorium auditorium, int availableSeats) {
        this.showtimeId = showtimeId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.format = format;
        this.languageType = languageType;
        this.auditorium = auditorium;
        this.availableSeats = availableSeats;
    }

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
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

    public Auditorium getAuditorium() {
        return auditorium;
    }

    public void setAuditorium(Auditorium auditorium) {
        this.auditorium = auditorium;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}

