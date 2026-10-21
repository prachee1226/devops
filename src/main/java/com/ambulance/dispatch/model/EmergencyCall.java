package com.ambulance.dispatch.model;

import java.time.LocalDateTime;

public class EmergencyCall {
    private String id;
    private String callerName;
    private String callerPhone;
    private String locationAddress;
    private String emergencyType; // Cardiac, Trauma, Respiratory, Stroke, Accident, General
    private String priority; // CRITICAL, HIGH, MEDIUM, LOW
    private String assignedAmbulanceId;
    private String status; // PENDING, DISPATCHED, ON_SCENE, RESOLVED, CANCELLED
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime dispatchedAt;
    private LocalDateTime resolvedAt;

    public EmergencyCall() {
        this.createdAt = LocalDateTime.now();
        this.status = "PENDING";
    }

    public EmergencyCall(String id, String callerName, String callerPhone, String locationAddress,
                         String emergencyType, String priority, String notes) {
        this.id = id;
        this.callerName = callerName;
        this.callerPhone = callerPhone;
        this.locationAddress = locationAddress;
        this.emergencyType = emergencyType;
        this.priority = priority;
        this.notes = notes;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCallerName() { return callerName; }
    public void setCallerName(String callerName) { this.callerName = callerName; }

    public String getCallerPhone() { return callerPhone; }
    public void setCallerPhone(String callerPhone) { this.callerPhone = callerPhone; }

    public String getLocationAddress() { return locationAddress; }
    public void setLocationAddress(String locationAddress) { this.locationAddress = locationAddress; }

    public String getEmergencyType() { return emergencyType; }
    public void setEmergencyType(String emergencyType) { this.emergencyType = emergencyType; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getAssignedAmbulanceId() { return assignedAmbulanceId; }
    public void setAssignedAmbulanceId(String assignedAmbulanceId) { this.assignedAmbulanceId = assignedAmbulanceId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getDispatchedAt() { return dispatchedAt; }
    public void setDispatchedAt(LocalDateTime dispatchedAt) { this.dispatchedAt = dispatchedAt; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
}
