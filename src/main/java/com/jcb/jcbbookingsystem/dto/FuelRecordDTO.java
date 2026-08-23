package com.jcb.jcbbookingsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FuelRecordDTO {
    private Long id;

    @NotNull(message = "Vehicle id is required")
    private Long vehicleId;

    @NotNull(message = "Fuel date is required")
    private LocalDate fuelDate;

    @NotNull(message = "Liters is required")
    private Double liters;

    private BigDecimal cost;
    private Double odometerReading;
}

