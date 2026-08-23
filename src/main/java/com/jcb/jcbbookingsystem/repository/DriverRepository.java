package com.jcb.jcbbookingsystem.repository;

import com.jcb.jcbbookingsystem.model.Driver;
import com.jcb.jcbbookingsystem.model.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    List<Driver> findByStatus(DriverStatus status);
}

