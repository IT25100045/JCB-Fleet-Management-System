package com.jcb.jcbbookingsystem.dto;

import com.jcb.jcbbookingsystem.model.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverDTO {
    private Long id;

    private String fullName;

    private String nic;

    private String phone;

    private String licenseNumber;

    private LocalDate licenseExpiryDate;

    private DriverStatus status;
}
