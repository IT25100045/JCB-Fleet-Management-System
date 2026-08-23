package com.jcb.jcbbookingsystem.dto;

import com.jcb.jcbbookingsystem.model.ShiftStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftResponse {
    private Long id;
    private Long driverId;
    private String driverName;
    private Long vehicleId;
    private String vehicleName;
    private Long bookingId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ShiftStatus status;
    private String notes;
}
