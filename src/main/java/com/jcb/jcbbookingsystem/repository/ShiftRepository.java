package com.jcb.jcbbookingsystem.repository;

import com.jcb.jcbbookingsystem.model.Shift;
import com.jcb.jcbbookingsystem.model.ShiftStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

    List<Shift> findByDriverId(Long driverId);

    List<Shift> findByDate(LocalDate date);

    List<Shift> findByStatus(ShiftStatus status);

    @Query("""
        SELECT s FROM Shift s
        WHERE s.driver.id = :driverId
          AND s.date = :date
          AND s.startTime < :endTime
          AND s.endTime > :startTime
          AND s.status <> com.jcb.jcbbookingsystem.model.ShiftStatus.CANCELLED
    """)
    List<Shift> findOverlappingShifts(
            @Param("driverId") Long driverId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
