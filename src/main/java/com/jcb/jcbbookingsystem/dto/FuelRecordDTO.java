package com.jcb.jcbbookingsystem.dto;

import java.time.LocalDate;

public class FuelRecordDTO {

    private Long jcbId;
    private LocalDate fuelDate;
    private Double litersFilled;
    private Double cost;
    private Double odometerReading;

    // Getters and Setters
    public Long getJcbId() { return jcbId; }
    public void setJcbId(Long jcbId) { this.jcbId = jcbId; }

    public LocalDate getFuelDate() { return fuelDate; }
    public void setFuelDate(LocalDate fuelDate) { this.fuelDate = fuelDate; }

    public Double getLitersFilled() { return litersFilled; }
    public void setLitersFilled(Double litersFilled) { this.litersFilled = litersFilled; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }

    public Double getOdometerReading() { return odometerReading; }
    public void setOdometerReading(Double odometerReading) { this.odometerReading = odometerReading; }
}