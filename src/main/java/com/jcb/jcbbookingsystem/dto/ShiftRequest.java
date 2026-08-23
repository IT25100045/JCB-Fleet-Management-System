package com.jcb.jcbbookingsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShiftRequest {

    @NotNull(message = "Driver id is required")
    private Long driverId;

    @NotNull(message = "Vehicle id is required")
    private Long vehicleId;

    private Long bookingId;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    private String notes;
}

