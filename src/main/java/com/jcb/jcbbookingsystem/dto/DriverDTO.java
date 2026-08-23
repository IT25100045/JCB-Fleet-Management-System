package com.jcb.jcbbookingsystem.dto;

import com.jcb.jcbbookingsystem.model.DriverStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DriverDTO {
    private Long id;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    private String phone;
    private String email;
    private DriverStatus status;
    private Integer yearsExperience;
}

