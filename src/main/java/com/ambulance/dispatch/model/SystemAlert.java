package com.ambulance.dispatch.model;

import java.time.LocalDateTime;

public class SystemAlert {
    private String id;
    private String alertType; // LOW_OXYGEN, LOW_FUEL, UNASSIGNED_CRITICAL, RESPONSE_DELAY, MAINTENANCE_REQUIRED
    private String severity; // CRITICAL, WARNING, INFO
    private String message;
    private String ambulanceId;
    private String callId;
    private LocalDateTime timestamp;
    private boolean resolved;

    public SystemAlert() {
        this.timestamp = LocalDateTime.now();
        this.resolved = false;
    }

    public SystemAlert(String id, String alertType, String severity, String message, String ambulanceId, String callId) {
        this.id = id;
        this.alertType = alertType;
        this.severity = severity;
        this.message = message;
        this.ambulanceId = ambulanceId;
        this.callId = callId;
        this.timestamp = LocalDateTime.now();
        this.resolved = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getAmbulanceId() { return ambulanceId; }
    public void setAmbulanceId(String ambulanceId) { this.ambulanceId = ambulanceId; }

    public String getCallId() { return callId; }
    public void setCallId(String callId) { this.callId = callId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public boolean isResolved() { return resolved; }
    public void setResolved(boolean resolved) { this.resolved = resolved; }
}
