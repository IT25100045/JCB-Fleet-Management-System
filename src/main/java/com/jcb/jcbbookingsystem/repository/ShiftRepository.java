package com.jcb.jcbbookingsystem.repository;

import com.jcb.jcbbookingsystem.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

    List<Shift> findByDriverId(Long driverId);

    List<Shift> findByVehicleId(Long vehicleId);

    @Query("SELECT s FROM Shift s WHERE s.driver.id = :driverId " +
            "AND s.status <> 'CANCELLED' " +
            "AND s.startTime <= :endTime AND s.endTime >= :startTime")
    List<Shift> findOverlappingShiftsForDriver(@Param("driverId") Long driverId,
                                               @Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime);
}

