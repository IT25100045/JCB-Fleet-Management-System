package com.jcb.jcbbookingsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fuel_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuelRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(name = "fuel_date", nullable = false)
    private LocalDate fuelDate;

    @Column(nullable = false)
    private Double liters;

    @Column(precision = 10, scale = 2)
    private BigDecimal cost;

    @Column(name = "odometer_reading")
    private Double odometerReading;
}
