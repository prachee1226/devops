package com.ambulance.dispatch.controller;

import com.ambulance.dispatch.model.Ambulance;
import com.ambulance.dispatch.model.DispatchEvent;
import com.ambulance.dispatch.service.AmbulanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ambulances")
@CrossOrigin(origins = "*")
public class AmbulanceController {

    private final AmbulanceService ambulanceService;

    public AmbulanceController(AmbulanceService ambulanceService) {
        this.ambulanceService = ambulanceService;
    }

    @GetMapping
    public List<Ambulance> listAmbulances(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String status) {
        if (query != null || status != null) {
            return ambulanceService.searchAmbulances(query, status);
        }
        return ambulanceService.getAllAmbulances();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ambulance> getAmbulance(@PathVariable String id) {
        return ambulanceService.getAmbulanceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ambulance> createAmbulance(@RequestBody Ambulance ambulance) {
        Ambulance created = ambulanceService.saveAmbulance(ambulance);
        return ResponseEntity.ok(created);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Ambulance> updateStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> payload) {
        String status = payload.get("status");
        if (status == null || status.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ambulanceService.updateStatus(id, status)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/events")
    public List<DispatchEvent> getAmbulanceEvents(@PathVariable String id) {
        return ambulanceService.getEventsForAmbulance(id);
    }
}
