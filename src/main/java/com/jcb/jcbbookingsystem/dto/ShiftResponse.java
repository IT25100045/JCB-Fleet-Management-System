package com.jcb.jcbbookingsystem.dto;

import com.jcb.jcbbookingsystem.model.ShiftStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftResponse {
    private Long id;

    private Long driverId;

    private String driverName;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private ShiftStatus status;
}

