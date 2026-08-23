package com.jcb.jcbbookingsystem.service;

import com.jcb.jcbbookingsystem.dto.DriverDTO;
import com.jcb.jcbbookingsystem.model.Driver;
import com.jcb.jcbbookingsystem.model.DriverStatus;
import com.jcb.jcbbookingsystem.repository.DriverRepository;
import com.jcb.jcbbookingsystem.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    public List<DriverDTO> getAll() {
        return driverRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<DriverDTO> getAvailable() {
        return driverRepository.findByStatus(DriverStatus.AVAILABLE).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public DriverDTO getById(Long id) {
        return toDTO(findEntity(id));
    }

    public DriverDTO create(DriverDTO dto) {
        Driver driver = Driver.builder()
                .fullName(dto.getFullName())
                .licenseNumber(dto.getLicenseNumber())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .status(dto.getStatus() != null ? dto.getStatus() : DriverStatus.AVAILABLE)
                .yearsExperience(dto.getYearsExperience())
                .build();
        return toDTO(driverRepository.save(driver));
    }

    public DriverDTO update(Long id, DriverDTO dto) {
        Driver existing = findEntity(id);
        existing.setFullName(dto.getFullName());
        existing.setLicenseNumber(dto.getLicenseNumber());
        existing.setPhone(dto.getPhone());
        existing.setEmail(dto.getEmail());
        if (dto.getStatus() != null) existing.setStatus(dto.getStatus());
        existing.setYearsExperience(dto.getYearsExperience());
        return toDTO(driverRepository.save(existing));
    }

    public void delete(Long id) {
        driverRepository.delete(findEntity(id));
    }

    public Driver findEntity(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + id));
    }

    private DriverDTO toDTO(Driver d) {
        DriverDTO dto = new DriverDTO();
        dto.setId(d.getId());
        dto.setFullName(d.getFullName());
        dto.setLicenseNumber(d.getLicenseNumber());
        dto.setPhone(d.getPhone());
        dto.setEmail(d.getEmail());
        dto.setStatus(d.getStatus());
        dto.setYearsExperience(d.getYearsExperience());
        return dto;
    }
}

