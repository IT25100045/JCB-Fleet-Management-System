package com.jcb.jcbbookingsystem.repository;

import com.jcb.jcbbookingsystem.model.FuelRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FuelRecordRepository extends JpaRepository<FuelRecord, Long> {
    List<FuelRecord> findByVehicleId(Long vehicleId);
}

