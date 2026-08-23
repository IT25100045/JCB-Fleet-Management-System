package com.jcb.jcbbookingsystem.service;

import com.jcb.jcbbookingsystem.exception.ResourceNotFoundException;
import com.jcb.jcbbookingsystem.dto.VehicleDTO;
import com.jcb.jcbbookingsystem.model.Vehicle;
import com.jcb.jcbbookingsystem.model.VehicleStatus;
import com.jcb.jcbbookingsystem.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public List<VehicleDTO> getAll() {
        return vehicleRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<VehicleDTO> getAvailable() {
        return vehicleRepository.findByStatus(VehicleStatus.AVAILABLE).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public VehicleDTO getById(Long id) {
        return toDTO(findEntity(id));
    }

    public VehicleDTO create(VehicleDTO dto) {
        Vehicle vehicle = toEntity(dto);
        return toDTO(vehicleRepository.save(vehicle));
    }

    public VehicleDTO update(Long id, VehicleDTO dto) {
        Vehicle existing = findEntity(id);
        existing.setName(dto.getName());
        existing.setType(dto.getType());
        existing.setRegistrationNumber(dto.getRegistrationNumber());
        existing.setModelNumber(dto.getModelNumber());
        existing.setPricePerDay(dto.getPricePerDay());
        if (dto.getStatus() != null) existing.setStatus(dto.getStatus());
        existing.setImageUrl(dto.getImageUrl());
        existing.setDescription(dto.getDescription());
        existing.setLocation(dto.getLocation());
        existing.setCurrentOdometer(dto.getCurrentOdometer());
        return toDTO(vehicleRepository.save(existing));
    }

    public void delete(Long id) {
        vehicleRepository.delete(findEntity(id));
    }

    public void updateStatus(Long id, VehicleStatus status) {
        Vehicle vehicle = findEntity(id);
        vehicle.setStatus(status);
        vehicleRepository.save(vehicle);
    }

    public Vehicle findEntity(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
    }

    private VehicleDTO toDTO(Vehicle v) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(v.getId());
        dto.setName(v.getName());
        dto.setType(v.getType());
        dto.setRegistrationNumber(v.getRegistrationNumber());
        dto.setModelNumber(v.getModelNumber());
        dto.setPricePerDay(v.getPricePerDay());
        dto.setStatus(v.getStatus());
        dto.setImageUrl(v.getImageUrl());
        dto.setDescription(v.getDescription());
        dto.setLocation(v.getLocation());
        dto.setCurrentOdometer(v.getCurrentOdometer());
        return dto;
    }

    private Vehicle toEntity(VehicleDTO dto) {
        return Vehicle.builder()
                .name(dto.getName())
                .type(dto.getType())
                .registrationNumber(dto.getRegistrationNumber())
                .modelNumber(dto.getModelNumber())
                .pricePerDay(dto.getPricePerDay())
                .status(dto.getStatus() != null ? dto.getStatus() : VehicleStatus.AVAILABLE)
                .imageUrl(dto.getImageUrl())
                .description(dto.getDescription())
                .location(dto.getLocation())
                .currentOdometer(dto.getCurrentOdometer())
                .build();
    }
}

