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
public class OvertimeResponse {
    private Long driverId;

    private LocalDate date;

    private double totalHours;

    private double normalHours;

    private double overtimeHours;
}
