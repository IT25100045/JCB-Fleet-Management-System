package com.jcb.jcbbookingsystem.repository;

import com.jcb.jcbbookingsystem.model.Vehicle;
import com.jcb.jcbbookingsystem.model.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByStatus(VehicleStatus status);
    List<Vehicle> findByTypeIgnoreCase(String type);
}
