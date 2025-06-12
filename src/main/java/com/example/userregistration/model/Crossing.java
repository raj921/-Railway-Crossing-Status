package com.example.userregistration.model;

import java.io.Serializable;

public class Crossing implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String name;
    private String address;
    private String landmark;
    private String trainSchedule;
    private String personInCharge;
    private String status;
    private String lastUpdated;
    private boolean isFavorite;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getTrainSchedule() {
        return trainSchedule;
    }

    public void setTrainSchedule(String trainSchedule) {
        this.trainSchedule = trainSchedule;
    }
    
    public String getPersonInCharge() {
        return personInCharge;
    }

    public void setPersonInCharge(String personInCharge) {
        this.personInCharge = personInCharge;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    public boolean getIsFavorite() {
        return isFavorite;
    }
    
    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}
