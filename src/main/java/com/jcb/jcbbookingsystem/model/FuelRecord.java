package com.jcb.jcbbookingsystem.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "fuel_records")
public class FuelRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long jcbId;                  // எந்த JCB-க்கு fuel entry

    private LocalDate fuelDate;          // எப்போ fuel போட்டது

    private Double litersFilled;         // எவ்வளவு liters

    private Double cost;                 // Fuel-க்கு ஆன செலவு

    private Double odometerReading;      // அந்த நேரத்துல machine hours/reading (efficiency calculate பண்ண)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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