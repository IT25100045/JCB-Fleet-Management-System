package com.jcb.jcbbookingsystem.repository;

import com.jcb.jcbbookingsystem.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}