package com.jcb.jcbbookingsystem.service;

import com.jcb.jcbbookingsystem.dto.DriverDTO;
import com.jcb.jcbbookingsystem.exception.ConflictException;
import com.jcb.jcbbookingsystem.exception.ResourceNotFoundException;
import com.jcb.jcbbookingsystem.model.Driver;
import com.jcb.jcbbookingsystem.model.DriverStatus;
import com.jcb.jcbbookingsystem.repository.DriverRepository;
import com.jcb.jcbbookingsystem.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverService {
    private final DriverRepository driverRepository;
    private final ShiftRepository shiftRepository;

    public DriverDTO createDriver(DriverDTO driverDTO) {

        if (driverRepository.existsByNic(driverDTO.getNic())) {
            throw new ConflictException("Driver with this NIC already exists");
        }

        if (driverRepository.existsByLicenseNumber(driverDTO.getLicenseNumber())) {
            throw new ConflictException("Driver with this license number already exists");
        }

        validateLicenseExpiry(driverDTO.getLicenseExpiryDate());

        Driver driver = Driver.builder()
                .fullName(driverDTO.getFullName())
                .nic(driverDTO.getNic())
                .phone(driverDTO.getPhone())
                .licenseNumber(driverDTO.getLicenseNumber())
                .licenseExpiryDate(driverDTO.getLicenseExpiryDate())
                .status(driverDTO.getStatus() != null
                        ? driverDTO.getStatus()
                        : DriverStatus.AVAILABLE)
                .build();

        Driver savedDriver = driverRepository.save(driver);

        return toDTO(savedDriver);
    }

    public List<DriverDTO> getAllDrivers() {
        return driverRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public DriverDTO getDriverById(Long id) {
        Driver driver = findEntity(id);
        return toDTO(driver);
    }

    public DriverDTO updateDriver(Long id, DriverDTO driverDTO) {

        Driver driver = findEntity(id);

        if (!driver.getNic().equals(driverDTO.getNic())
                && driverRepository.existsByNic(driverDTO.getNic())) {
            throw new ConflictException("Driver with this NIC already exists");
        }

        if (!driver.getLicenseNumber().equals(driverDTO.getLicenseNumber())
                && driverRepository.existsByLicenseNumber(driverDTO.getLicenseNumber())) {
            throw new ConflictException("Driver with this license number already exists");
        }

        validateLicenseExpiry(driverDTO.getLicenseExpiryDate());

        driver.setFullName(driverDTO.getFullName());
        driver.setNic(driverDTO.getNic());
        driver.setPhone(driverDTO.getPhone());
        driver.setLicenseNumber(driverDTO.getLicenseNumber());
        driver.setLicenseExpiryDate(driverDTO.getLicenseExpiryDate());

        if (driverDTO.getStatus() != null) {
            driver.setStatus(driverDTO.getStatus());
        }

        Driver updatedDriver = driverRepository.save(driver);

        return toDTO(updatedDriver);
    }

    public void deleteDriver(Long id) {
        Driver driver = findEntity(id);
        driver.setStatus(DriverStatus.INACTIVE);
        driverRepository.save(driver);
    }

    public List<DriverDTO> getDriversByStatus(DriverStatus status) {
        return driverRepository.findByStatus(status)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<DriverDTO> getAvailableDrivers(
            LocalDate date,
            java.time.LocalTime startTime,
            java.time.LocalTime endTime) {

        return driverRepository.findByStatus(DriverStatus.AVAILABLE)
                .stream()
                .filter(driver ->
                        !driver.getLicenseExpiryDate().isBefore(date))
                .filter(driver ->
                        shiftRepository.findOverlappingShifts(
                                driver.getId(),
                                date,
                                startTime,
                                endTime
                        ).isEmpty())
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Driver findEntity(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Driver not found with id: " + id));
    }

    private void validateLicenseExpiry(LocalDate licenseExpiryDate) {

        if (licenseExpiryDate == null) {
            throw new ConflictException("License expiry date is required");
        }

        if (licenseExpiryDate.isBefore(LocalDate.now())) {
            throw new ConflictException("Driver license has expired");
        }
    }

    private DriverDTO toDTO(Driver driver) {

        return DriverDTO.builder()
                .id(driver.getId())
                .fullName(driver.getFullName())
                .nic(driver.getNic())
                .phone(driver.getPhone())
                .licenseNumber(driver.getLicenseNumber())
                .licenseExpiryDate(driver.getLicenseExpiryDate())
                .status(driver.getStatus())
                .build();
    }
}
