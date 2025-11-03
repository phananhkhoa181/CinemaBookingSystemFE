package com.example.cinemabookingsystemfe.data.models.response.showtimes;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Auditorium implements Serializable {
    @SerializedName("auditoriumid")
    private int auditoriumId;

    @SerializedName("name")
    private String name;

    @SerializedName("capacity")
    private int capacity;

    public Auditorium() {}

    public Auditorium(int auditoriumId, String name, int capacity) {
        this.auditoriumId = auditoriumId;
        this.name = name;
        this.capacity = capacity;
    }

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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}

