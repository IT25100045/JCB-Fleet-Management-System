package com.jcb.jcbbookingsystem.dto;

import com.jcb.jcbbookingsystem.model.IncidentSeverity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IncidentReportRequest {

    private Long vehicleId;
    private Long bookingId;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Incident date is required")
    private LocalDateTime incidentDate;

    private IncidentSeverity severity;
}

