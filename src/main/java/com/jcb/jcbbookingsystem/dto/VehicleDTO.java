package com.jcb.jcbbookingsystem.dto;

import com.jcb.jcbbookingsystem.model.VehicleStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class VehicleDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String type;

    private String registrationNumber;

    private String modelNumber;

    @NotNull(message = "Price per day is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal pricePerDay;

    private VehicleStatus status;

    private String imageUrl;

    private String description;

    private String location;

    private Double currentOdometer;
}

