package com.jcb.jcbbookingsystem.dto;

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
public class ShiftRequest {
    private Long driverId;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;
}
