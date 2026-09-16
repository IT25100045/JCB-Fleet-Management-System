package com.jcb.jcbbookingsystem.dto;

import com.jcb.jcbbookingsystem.model.VehicleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class VehicleDTO {

    private Long id;

    @NotBlank(message = "Model is required")
    private String model;

    @Positive(message = "Engine capacity must be positive")
    private Double engineCapacity;

    @NotNull(message = "Hourly rate is required")
    @Positive(message = "Hourly rate must be positive")
    private Double hourlyRate;

    @NotBlank(message = "Registration ID is required")
    private String registrationId;

    private VehicleStatus status;
}
