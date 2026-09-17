package com.jcb.jcbbookingsystem.service;

import com.jcb.jcbbookingsystem.dto.VehicleDTO;
import com.jcb.jcbbookingsystem.model.Vehicle;
import com.jcb.jcbbookingsystem.model.VehicleStatus;
import com.jcb.jcbbookingsystem.repository.VehicleRepository;
import com.jcb.jcbbookingsystem.exception.DuplicateResourceException;
import com.jcb.jcbbookingsystem.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<VehicleDTO> getAll() {
        return vehicleRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public VehicleDTO getById(Long id) {
        return toDTO(findEntity(id));
    }

    public List<VehicleDTO> getByStatus(VehicleStatus status) {
        return vehicleRepository.findByStatus(status).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<VehicleDTO> search(String model) {
        return vehicleRepository.findByModelContainingIgnoreCase(model).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public VehicleDTO create(VehicleDTO dto) {
        if (vehicleRepository.existsByRegistrationId(dto.getRegistrationId())) {
            throw new DuplicateResourceException("A vehicle with this registration ID already exists");
        }
        Vehicle vehicle = Vehicle.builder()
                .model(dto.getModel())
                .engineCapacity(dto.getEngineCapacity())
                .hourlyRate(dto.getHourlyRate())
                .registrationId(dto.getRegistrationId())
                .status(dto.getStatus() != null ? dto.getStatus() : VehicleStatus.AVAILABLE)
                .build();
        return toDTO(vehicleRepository.save(vehicle));
    }

    public VehicleDTO update(Long id, VehicleDTO dto) {
        Vehicle vehicle = findEntity(id);