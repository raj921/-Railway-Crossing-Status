package com.example.userregistration;

import java.sql.Timestamp;

/**
 * Represents a railway crossing in the system.
 */
public class RailwayCrossing {
    private int id;
    private String name;
    private String address;
    private String landmark;
    private String trainSchedule;
    private String personInCharge;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int createdBy;
    private byte[] photo; // For storing photo data

    // Constructor
    public RailwayCrossing() {
    }
    
    public RailwayCrossing(int id, String name, String address, String landmark, 
                          String trainSchedule, String personInCharge, String status) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.landmark = landmark;
        this.trainSchedule = trainSchedule;
        this.personInCharge = personInCharge;
        this.status = status;
    }
    
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    // Helper method to get status badge HTML
    public String getStatusBadge() {
        if (status == null) return "";
        
        String badgeClass = "bg-secondary";
        if ("Active".equalsIgnoreCase(status)) {
            badgeClass = "bg-success";
        } else if ("Inactive".equalsIgnoreCase(status)) {
            badgeClass = "bg-danger";
        } else if ("Under Maintenance".equalsIgnoreCase(status)) {
            badgeClass = "bg-warning text-dark";
        }
        
        return String.format("<span class='badge %s'>%s</span>", badgeClass, status);
    }
}
