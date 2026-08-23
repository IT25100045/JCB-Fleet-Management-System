package com.jcb.jcbbookingsystem.repository;

import com.jcb.jcbbookingsystem.model.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long> {
    List<MaintenanceRecord> findByVehicleId(Long vehicleId);
}
