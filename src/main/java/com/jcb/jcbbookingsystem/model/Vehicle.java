package com.jcb.jcbbookingsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 50)
    private String type; // e.g. Excavator, Backhoe Loader, Bulldozer

    @Column(name = "registration_number", unique = true, length = 30)
    private String registrationNumber;

    @Column(name = "model_number", length = 50)
    private String modelNumber;

    @Column(name = "price_per_day", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerDay;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private VehicleStatus status = VehicleStatus.AVAILABLE;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(length = 1000)
    private String description;

    @Column(length = 100)
    private String location;

    @Column(name = "current_odometer")
    private Double currentOdometer;
}
