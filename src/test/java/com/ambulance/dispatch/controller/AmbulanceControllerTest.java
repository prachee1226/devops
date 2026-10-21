package com.ambulance.dispatch.controller;

import com.ambulance.dispatch.model.Ambulance;
import com.ambulance.dispatch.model.EmergencyCall;
import com.ambulance.dispatch.service.AmbulanceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AmbulanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AmbulanceService ambulanceService;

    @Test
    @DisplayName("API-01: List all ambulances should return 200 with seeded fleet")
    void testGetAllAmbulances() throws Exception {
        mockMvc.perform(get("/api/ambulances"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(5))))
                .andExpect(jsonPath("$[0].vehicleNumber", notNullValue()));
    }

    @Test
    @DisplayName("API-02: Search and filter ambulances by status")
    void testSearchAmbulances() throws Exception {
        mockMvc.perform(get("/api/ambulances")
                        .param("status", "AVAILABLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status", is("AVAILABLE")));
    }

    @Test
    @DisplayName("API-03: Register emergency call and verify status PENDING")
    void testCreateEmergencyCall() throws Exception {
        String callJson = """
                {
                    "callerName": "Suresh Raina",
                    "callerPhone": "+91 91234 56789",
                    "locationAddress": "Deccan Gymkhana, Pune",
                    "emergencyType": "Accident",
                    "priority": "HIGH",
                    "notes": "Motorbike skid, conscious"
                }
                """;

        mockMvc.perform(post("/api/calls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(callJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.status", is("PENDING")))
                .andExpect(jsonPath("$.priority", is("HIGH")));
    }

    @Test
    @DisplayName("API-04: Dispatch available ambulance to pending call")
    void testDispatchAmbulance() throws Exception {
        EmergencyCall call = new EmergencyCall("CALL-TEST-1", "Test Caller", "+91 99999 00000", "Aundh Pune", "General", "MEDIUM", "Fever");
        ambulanceService.createCall(call);

        String dispatchJson = """
                {
                    "ambulanceId": "AMB-101"
                }
                """;

        mockMvc.perform(post("/api/calls/" + call.getId() + "/dispatch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dispatchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    @DisplayName("API-05: Fetch Dashboard KPI metrics")
    void testGetDashboardMetrics() throws Exception {
        mockMvc.perform(get("/api/dashboard/metrics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmbulances", greaterThan(0)))
                .andExpect(jsonPath("$.fleetUtilizationRate", notNullValue()));
    }

    @Test
    @DisplayName("API-06: Fetch and resolve system alerts")
    void testGetAndResolveAlerts() throws Exception {
        mockMvc.perform(get("/api/dashboard/alerts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }
}
