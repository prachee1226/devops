package com.ambulance.dispatch.model;

public class DashboardMetrics {
    private int totalAmbulances;
    private int availableUnits;
    private int dispatchedUnits;
    private int onSceneUnits;
    private int atHospitalUnits;
    private int outOfServiceUnits;
    private int activeEmergencies;
    private int criticalAlerts;
    private double avgResponseTimeMinutes;
    private double fleetUtilizationRate;

    public DashboardMetrics() {}

    public int getTotalAmbulances() { return totalAmbulances; }
    public void setTotalAmbulances(int totalAmbulances) { this.totalAmbulances = totalAmbulances; }

    public int getAvailableUnits() { return availableUnits; }
    public void setAvailableUnits(int availableUnits) { this.availableUnits = availableUnits; }

    public int getDispatchedUnits() { return dispatchedUnits; }
    public void setDispatchedUnits(int dispatchedUnits) { this.dispatchedUnits = dispatchedUnits; }

    public int getOnSceneUnits() { return onSceneUnits; }
    public void setOnSceneUnits(int onSceneUnits) { this.onSceneUnits = onSceneUnits; }

    public int getAtHospitalUnits() { return atHospitalUnits; }
    public void setAtHospitalUnits(int atHospitalUnits) { this.atHospitalUnits = atHospitalUnits; }

    public int getOutOfServiceUnits() { return outOfServiceUnits; }
    public void setOutOfServiceUnits(int outOfServiceUnits) { this.outOfServiceUnits = outOfServiceUnits; }

    public int getActiveEmergencies() { return activeEmergencies; }
    public void setActiveEmergencies(int activeEmergencies) { this.activeEmergencies = activeEmergencies; }

    public int getCriticalAlerts() { return criticalAlerts; }
    public void setCriticalAlerts(int criticalAlerts) { this.criticalAlerts = criticalAlerts; }

    public double getAvgResponseTimeMinutes() { return avgResponseTimeMinutes; }
    public void setAvgResponseTimeMinutes(double avgResponseTimeMinutes) { this.avgResponseTimeMinutes = avgResponseTimeMinutes; }

    public double getFleetUtilizationRate() { return fleetUtilizationRate; }
    public void setFleetUtilizationRate(double fleetUtilizationRate) { this.fleetUtilizationRate = fleetUtilizationRate; }
}
