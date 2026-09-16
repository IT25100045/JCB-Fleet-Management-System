package com.jcb.jcbbookingsystem.service;

import com.jcb.system.exception.DuplicateResourceException;
import com.jcb.system.exception.ResourceNotFoundException;
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
        vehicle.setModel(dto.getModel());
        vehicle.setEngineCapacity(dto.getEngineCapacity());
        vehicle.setHourlyRate(dto.getHourlyRate());
        vehicle.setRegistrationId(dto.getRegistrationId());
        if (dto.getStatus() != null) {
            vehicle.setStatus(dto.getStatus());
        }
        return toDTO(vehicleRepository.save(vehicle));
    }

    public VehicleDTO updateStatus(Long id, VehicleStatus status) {
        Vehicle vehicle = findEntity(id);
        vehicle.setStatus(status);
        return toDTO(vehicleRepository.save(vehicle));
    }

    public void delete(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicle not found with id: " + id);
        }
        vehicleRepository.deleteById(id);
    }

    private Vehicle findEntity(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
    }

    private VehicleDTO toDTO(Vehicle v) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(v.getId());
        dto.setModel(v.getModel());
        dto.setEngineCapacity(v.getEngineCapacity());
        dto.setHourlyRate(v.getHourlyRate());
        dto.setRegistrationId(v.getRegistrationId());
        dto.setStatus(v.getStatus());
        return dto;
    }
}
