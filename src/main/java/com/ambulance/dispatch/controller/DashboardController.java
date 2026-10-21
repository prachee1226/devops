package com.ambulance.dispatch.controller;

import com.ambulance.dispatch.model.DashboardMetrics;
import com.ambulance.dispatch.model.SystemAlert;
import com.ambulance.dispatch.service.AmbulanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final AmbulanceService ambulanceService;

    public DashboardController(AmbulanceService ambulanceService) {
        this.ambulanceService = ambulanceService;
    }

    @GetMapping("/metrics")
    public DashboardMetrics getMetrics() {
        return ambulanceService.getMetrics();
    }

    @GetMapping("/alerts")
    public List<SystemAlert> getAlerts() {
        return ambulanceService.getActiveAlerts();
    }

    @PostMapping("/alerts/{id}/resolve")
    public ResponseEntity<Map<String, Object>> resolveAlert(@PathVariable String id) {
        boolean success = ambulanceService.resolveAlert(id);
        if (success) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Alert " + id + " marked as resolved"));
        }
        return ResponseEntity.notFound().build();
    }
}
