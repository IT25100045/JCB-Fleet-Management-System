package com.jcb.jcbbookingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkloadResponse {
    private Long driverId;

    private LocalDate date;

    private double totalHours;

    private double maximumHours;

    private double remainingHours;

    private boolean workloadExceeded;
}
