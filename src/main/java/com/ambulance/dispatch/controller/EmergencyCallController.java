package com.ambulance.dispatch.controller;

import com.ambulance.dispatch.model.EmergencyCall;
import com.ambulance.dispatch.service.AmbulanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/calls")
@CrossOrigin(origins = "*")
public class EmergencyCallController {

    private final AmbulanceService ambulanceService;

    public EmergencyCallController(AmbulanceService ambulanceService) {
        this.ambulanceService = ambulanceService;
    }

    @GetMapping
    public List<EmergencyCall> listCalls() {
        return ambulanceService.getAllCalls();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmergencyCall> getCall(@PathVariable String id) {
        return ambulanceService.getCallById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EmergencyCall> createCall(@RequestBody EmergencyCall call) {
        EmergencyCall created = ambulanceService.createCall(call);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/{id}/dispatch")
    public ResponseEntity<Map<String, Object>> dispatchAmbulance(
            @PathVariable String id,
            @RequestBody Map<String, String> payload) {
        String ambulanceId = payload.get("ambulanceId");
        if (ambulanceId == null || ambulanceId.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "ambulanceId is required"));
        }

        boolean success = ambulanceService.dispatchAmbulance(id, ambulanceId);
        if (success) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Ambulance " + ambulanceId + " dispatched for call " + id));
        } else {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invalid call or ambulance ID"));
        }
    }
}
