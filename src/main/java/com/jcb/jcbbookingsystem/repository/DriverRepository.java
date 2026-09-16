package com.jcb.jcbbookingsystem.repository;

import com.jcb.jcbbookingsystem.model.Driver;
import com.jcb.jcbbookingsystem.model.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByNic(String nic);

    Optional<Driver> findByLicenseNumber(String licenseNumber);

    List<Driver> findByStatus(DriverStatus status);

    boolean existsByNic(String nic);

    boolean existsByLicenseNumber(String licenseNumber);
}
