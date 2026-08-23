package com.jcb.jcbbookingsystem.dto;

import com.jcb.jcbbookingsystem.model.IncidentSeverity;
import com.jcb.jcbbookingsystem.model.IncidentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentReportResponse {
    private Long id;
    private Long vehicleId;
    private Long bookingId;
    private Long reportedById;
    private String reportedByUsername;
    private String description;
    private LocalDateTime incidentDate;
    private IncidentSeverity severity;
    private IncidentStatus status;
    private LocalDateTime createdAt;
}

