package com.ambulance.dispatch.service;

import com.ambulance.dispatch.model.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class AmbulanceService {

    private final Map<String, Ambulance> ambulances = new ConcurrentHashMap<>();
    private final Map<String, EmergencyCall> emergencyCalls = new ConcurrentHashMap<>();
    private final List<DispatchEvent> dispatchEvents = Collections.synchronizedList(new ArrayList<>());
    private final Map<String, SystemAlert> alerts = new ConcurrentHashMap<>();
    
    private final AtomicLong callSeq = new AtomicLong(100);
    private final AtomicLong eventSeq = new AtomicLong(1000);
    private final AtomicLong alertSeq = new AtomicLong(500);

    public AmbulanceService() {
        initSampleData();
    }

    private void initSampleData() {
        // Sample Ambulances
        saveAmbulance(new Ambulance("AMB-101", "MH-12-RN-1010", "Rajesh Sharma", "+91 98220 11223", "AVAILABLE", "Shivaji Nagar Hub", "Sassoon General Hospital", 95, 82));
        saveAmbulance(new Ambulance("AMB-102", "MH-12-RN-2020", "Pooja Kulkarni", "+91 98220 22334", "DISPATCHED", "Kothrud Depot", "Deenanath Mangeshkar Hospital", 88, 65));
        saveAmbulance(new Ambulance("AMB-103", "MH-12-RN-3030", "Amit Patil", "+91 98220 33445", "ON_SCENE", "Hinjawadi Phase 1", "Ruby Hall Clinic Hinjawadi", 90, 70));
        saveAmbulance(new Ambulance("AMB-104", "MH-12-RN-4040", "Sunita Deshmukh", "+91 98220 44556", "EN_ROUTE_HOSPITAL", "Hadapsar Bypass", "Noble Hospital Hadapsar", 40, 50));
        saveAmbulance(new Ambulance("AMB-105", "MH-12-RN-5050", "Vikas More", "+91 98220 55667", "AVAILABLE", "Viman Nagar Stand", "Sahyadri Hospital", 15, 90)); // Low oxygen
        saveAmbulance(new Ambulance("AMB-106", "MH-12-RN-6060", "Sanjay Jadhav", "+91 98220 66778", "OUT_OF_SERVICE", "Swargate Depot", "Jehangir Hospital", 95, 10)); // Low fuel / service

        // Sample Calls
        EmergencyCall c1 = new EmergencyCall("CALL-101", "Dr. Ramesh Joshi", "+91 94220 77889", "Sector 4, Hinjawadi", "Cardiac", "CRITICAL", "Severe chest pain and shortness of breath.");
        c1.setStatus("ON_SCENE");
        c1.setAssignedAmbulanceId("AMB-103");
        c1.setDispatchedAt(LocalDateTime.now().minusMinutes(12));
        emergencyCalls.put(c1.getId(), c1);

        EmergencyCall c2 = new EmergencyCall("CALL-102", "Anita Shinde", "+91 98900 88990", "FC Road, Deccan", "Trauma", "HIGH", "Two-wheeler collision with minor head injury.");
        c2.setStatus("DISPATCHED");
        c2.setAssignedAmbulanceId("AMB-102");
        c2.setDispatchedAt(LocalDateTime.now().minusMinutes(5));
        emergencyCalls.put(c2.getId(), c2);

        EmergencyCall c3 = new EmergencyCall("CALL-103", "Ganesh Gaikwad", "+91 97654 32109", "Magarpatta City", "Accident", "CRITICAL", "Pedestrian hit by car, immediate triage needed.");
        emergencyCalls.put(c3.getId(), c3);

        // Sample Events for Drill-down
        logEvent("AMB-103", "CALL-101", "CALL_RECEIVED", "Emergency call 108 logged for Cardiac patient.");
        logEvent("AMB-103", "CALL-101", "UNIT_ASSIGNED", "Ambulance AMB-103 assigned to Hinjawadi sector.");
        logEvent("AMB-103", "CALL-101", "DEPARTED_STATION", "Ambulance en route from depot, sirens activated.");
        logEvent("AMB-103", "CALL-101", "ARRIVED_AT_SCENE", "Paramedics on scene. Patient vitals being stabilized.");

        logEvent("AMB-102", "CALL-102", "CALL_RECEIVED", "Traffic accident reported at FC Road.");
        logEvent("AMB-102", "CALL-102", "UNIT_ASSIGNED", "AMB-102 dispatched with Trauma care kit.");
        logEvent("AMB-102", "CALL-102", "DEPARTED_STATION", "Estimated arrival time 6 minutes.");

        // Sample System Alerts
        addAlert("LOW_OXYGEN", "CRITICAL", "Ambulance AMB-105 oxygen cylinder is critically low at 15%. Restock required.", "AMB-105", null);
        addAlert("UNASSIGNED_CRITICAL", "CRITICAL", "Critical Emergency CALL-103 (Magarpatta City) is awaiting ambulance assignment!", null, "CALL-103");
        addAlert("LOW_FUEL", "WARNING", "Ambulance AMB-106 fuel level is at 10%. Refueling needed.", "AMB-106", null);
    }

    public List<Ambulance> getAllAmbulances() {
        return new ArrayList<>(ambulances.values());
    }

    public Optional<Ambulance> getAmbulanceById(String id) {
        return Optional.ofNullable(ambulances.get(id));
    }

    public Ambulance saveAmbulance(Ambulance ambulance) {
        if (ambulance.getId() == null || ambulance.getId().isBlank()) {
            ambulance.setId("AMB-" + (100 + ambulances.size() + 1));
        }
        ambulances.put(ambulance.getId(), ambulance);
        checkAlertsForAmbulance(ambulance);
        return ambulance;
    }

    public Optional<Ambulance> updateStatus(String id, String status) {
        Ambulance ambulance = ambulances.get(id);
        if (ambulance != null) {
            ambulance.setStatus(status);
            ambulance.setLastUpdated(LocalDateTime.now());
            logEvent(id, ambulance.getCurrentCallId(), "STATUS_CHANGE", "Status changed to " + status);
            return Optional.of(ambulance);
        }
        return Optional.empty();
    }

    public List<EmergencyCall> getAllCalls() {
        return emergencyCalls.values().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public Optional<EmergencyCall> getCallById(String id) {
        return Optional.ofNullable(emergencyCalls.get(id));
    }

    public EmergencyCall createCall(EmergencyCall call) {
        if (call.getId() == null || call.getId().isBlank()) {
            call.setId("CALL-" + callSeq.incrementAndGet());
        }
        call.setCreatedAt(LocalDateTime.now());
        call.setStatus("PENDING");
        emergencyCalls.put(call.getId(), call);

        logEvent(null, call.getId(), "CALL_RECEIVED", "Emergency intake registered for " + call.getCallerName() + " (" + call.getEmergencyType() + ")");

        if ("CRITICAL".equalsIgnoreCase(call.getPriority())) {
            addAlert("UNASSIGNED_CRITICAL", "CRITICAL", "Critical Priority Call " + call.getId() + " logged: " + call.getLocationAddress(), null, call.getId());
        }
        return call;
    }

    public synchronized boolean dispatchAmbulance(String callId, String ambulanceId) {
        EmergencyCall call = emergencyCalls.get(callId);
        Ambulance ambulance = ambulances.get(ambulanceId);

        if (call != null && ambulance != null) {
            ambulance.setStatus("DISPATCHED");
            ambulance.setCurrentCallId(callId);

            call.setAssignedAmbulanceId(ambulanceId);
            call.setStatus("DISPATCHED");
            call.setDispatchedAt(LocalDateTime.now());

            logEvent(ambulanceId, callId, "UNIT_ASSIGNED", "Ambulance " + ambulance.getVehicleNumber() + " dispatched to " + call.getLocationAddress());
            
            // Resolve unassigned alert if present
            alerts.values().stream()
                    .filter(a -> callId.equals(a.getCallId()) && "UNASSIGNED_CRITICAL".equals(a.getAlertType()))
                    .forEach(a -> a.setResolved(true));

            return true;
        }
        return false;
    }

    public List<DispatchEvent> getEventsForAmbulance(String ambulanceId) {
        return dispatchEvents.stream()
                .filter(e -> ambulanceId.equalsIgnoreCase(e.getAmbulanceId()))
                .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
                .collect(Collectors.toList());
    }

    public List<DispatchEvent> getEventsForCall(String callId) {
        return dispatchEvents.stream()
                .filter(e -> callId.equalsIgnoreCase(e.getCallId()))
                .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
                .collect(Collectors.toList());
    }

    public DashboardMetrics getMetrics() {
        DashboardMetrics m = new DashboardMetrics();
        m.setTotalAmbulances(ambulances.size());
        m.setAvailableUnits((int) ambulances.values().stream().filter(a -> "AVAILABLE".equalsIgnoreCase(a.getStatus())).count());
        m.setDispatchedUnits((int) ambulances.values().stream().filter(a -> "DISPATCHED".equalsIgnoreCase(a.getStatus())).count());
        m.setOnSceneUnits((int) ambulances.values().stream().filter(a -> "ON_SCENE".equalsIgnoreCase(a.getStatus())).count());
        m.setAtHospitalUnits((int) ambulances.values().stream().filter(a -> "AT_HOSPITAL".equalsIgnoreCase(a.getStatus()) || "EN_ROUTE_HOSPITAL".equalsIgnoreCase(a.getStatus())).count());
        m.setOutOfServiceUnits((int) ambulances.values().stream().filter(a -> "OUT_OF_SERVICE".equalsIgnoreCase(a.getStatus())).count());
        
        m.setActiveEmergencies((int) emergencyCalls.values().stream().filter(c -> !"RESOLVED".equalsIgnoreCase(c.getStatus()) && !"CANCELLED".equalsIgnoreCase(c.getStatus())).count());
        m.setCriticalAlerts((int) alerts.values().stream().filter(a -> !a.isResolved() && "CRITICAL".equalsIgnoreCase(a.getSeverity())).count());
        m.setAvgResponseTimeMinutes(8.4); // Target < 10 mins

        int inUse = m.getDispatchedUnits() + m.getOnSceneUnits() + m.getAtHospitalUnits();
        double utilization = m.getTotalAmbulances() > 0 ? ((double) inUse / m.getTotalAmbulances()) * 100.0 : 0.0;
        m.setFleetUtilizationRate(Math.round(utilization * 10.0) / 10.0);

        return m;
    }

    public List<SystemAlert> getActiveAlerts() {
        return alerts.values().stream()
                .filter(a -> !a.isResolved())
                .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
                .collect(Collectors.toList());
    }

    public boolean resolveAlert(String alertId) {
        SystemAlert alert = alerts.get(alertId);
        if (alert != null) {
            alert.setResolved(true);
            return true;
        }
        return false;
    }

    public List<Ambulance> searchAmbulances(String query, String status) {
        return ambulances.values().stream()
                .filter(a -> {
                    boolean matchesStatus = (status == null || status.isBlank() || "ALL".equalsIgnoreCase(status) || a.getStatus().equalsIgnoreCase(status));
                    if (!matchesStatus) return false;
                    if (query == null || query.isBlank()) return true;
                    String q = query.toLowerCase();
                    return a.getId().toLowerCase().contains(q) ||
                           a.getVehicleNumber().toLowerCase().contains(q) ||
                           a.getDriverName().toLowerCase().contains(q) ||
                           a.getCurrentLocation().toLowerCase().contains(q) ||
                           a.getAssignedHospital().toLowerCase().contains(q);
                })
                .collect(Collectors.toList());
    }

    private void logEvent(String ambulanceId, String callId, String eventType, String remarks) {
        DispatchEvent event = new DispatchEvent("EVT-" + eventSeq.incrementAndGet(), ambulanceId, callId, eventType, remarks);
        dispatchEvents.add(event);
    }

    private void addAlert(String type, String severity, String message, String ambId, String callId) {
        String id = "ALT-" + alertSeq.incrementAndGet();
        SystemAlert alert = new SystemAlert(id, type, severity, message, ambId, callId);
        alerts.put(id, alert);
    }

    private void checkAlertsForAmbulance(Ambulance ambulance) {
        if (ambulance.getOxygenPercent() < 20) {
            addAlert("LOW_OXYGEN", "CRITICAL", "Ambulance " + ambulance.getId() + " (" + ambulance.getVehicleNumber() + ") oxygen is critically low: " + ambulance.getOxygenPercent() + "%", ambulance.getId(), null);
        }
        if (ambulance.getFuelPercent() < 15) {
            addAlert("LOW_FUEL", "WARNING", "Ambulance " + ambulance.getId() + " fuel is below safety threshold: " + ambulance.getFuelPercent() + "%", ambulance.getId(), null);
        }
    }
}
