package com.jcb.jcbbookingsystem.dto;

import com.jcb.jcbbookingsystem.model.MaintenanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MaintenanceRecordDTO {
    private Long id;

    @NotNull(message = "Vehicle id is required")
    private Long vehicleId;

    @NotNull(message = "Service date is required")
    private LocalDate serviceDate;

    private String description;
    private BigDecimal cost;
    private LocalDate nextServiceDate;
    private MaintenanceStatus status;
    private String performedBy;
}
