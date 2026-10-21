package com.ambulance.dispatch.model;

import java.time.LocalDateTime;

public class DispatchEvent {
    private String id;
    private String ambulanceId;
    private String callId;
    private String eventType; // CALL_RECEIVED, UNIT_ASSIGNED, DEPARTED_STATION, ARRIVED_AT_SCENE, TRANSPORTING_PATIENT, ARRIVED_AT_HOSPITAL, CASE_RESOLVED
    private String remarks;
    private LocalDateTime timestamp;

    public DispatchEvent() {
        this.timestamp = LocalDateTime.now();
    }

    public DispatchEvent(String id, String ambulanceId, String callId, String eventType, String remarks) {
        this.id = id;
        this.ambulanceId = ambulanceId;
        this.callId = callId;
        this.eventType = eventType;
        this.remarks = remarks;
        this.timestamp = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAmbulanceId() { return ambulanceId; }
    public void setAmbulanceId(String ambulanceId) { this.ambulanceId = ambulanceId; }

    public String getCallId() { return callId; }
    public void setCallId(String callId) { this.callId = callId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
