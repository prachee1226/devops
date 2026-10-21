package com.ambulance.dispatch.model;

import java.time.LocalDateTime;

public class Ambulance {
    private String id;
    private String vehicleNumber;
    private String driverName;
    private String driverPhone;
    private String status; // AVAILABLE, DISPATCHED, ON_SCENE, EN_ROUTE_HOSPITAL, AT_HOSPITAL, OUT_OF_SERVICE
    private String currentLocation;
    private String assignedHospital;
    private int oxygenPercent;
    private int fuelPercent;
    private String currentCallId;
    private LocalDateTime lastUpdated;

    public Ambulance() {
        this.lastUpdated = LocalDateTime.now();
    }

    public Ambulance(String id, String vehicleNumber, String driverName, String driverPhone,
                     String status, String currentLocation, String assignedHospital,
                     int oxygenPercent, int fuelPercent) {
        this.id = id;
        this.vehicleNumber = vehicleNumber;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
        this.status = status;
        this.currentLocation = currentLocation;
        this.assignedHospital = assignedHospital;
        this.oxygenPercent = oxygenPercent;
        this.fuelPercent = fuelPercent;
        this.lastUpdated = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public String getDriverPhone() { return driverPhone; }
    public void setDriverPhone(String driverPhone) { this.driverPhone = driverPhone; }

    public String getStatus() { return status; }
    public void setStatus(String status) { 
        this.status = status; 
        this.lastUpdated = LocalDateTime.now();
    }

    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }

    public String getAssignedHospital() { return assignedHospital; }
    public void setAssignedHospital(String assignedHospital) { this.assignedHospital = assignedHospital; }

    public int getOxygenPercent() { return oxygenPercent; }
    public void setOxygenPercent(int oxygenPercent) { this.oxygenPercent = oxygenPercent; }

    public int getFuelPercent() { return fuelPercent; }
    public void setFuelPercent(int fuelPercent) { this.fuelPercent = fuelPercent; }

    public String getCurrentCallId() { return currentCallId; }
    public void setCurrentCallId(String currentCallId) { this.currentCallId = currentCallId; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}
