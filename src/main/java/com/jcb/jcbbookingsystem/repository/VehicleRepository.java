package com.jcb.jcbbookingsystem.repository;

import com.jcb.jcbbookingsystem.model.Vehicle;
import com.jcb.jcbbookingsystem.model.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    boolean existsByRegistrationId(String registrationId);
    Optional<Vehicle> findByRegistrationId(String registrationId);
    List<Vehicle> findByStatus(VehicleStatus status);
    List<Vehicle> findByModelContainingIgnoreCase(String model);
}