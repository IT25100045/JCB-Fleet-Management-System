package com.jcb.jcbbookingsystem.service;

import com.jcb.jcbbookingsystem.exception.ResourceNotFoundException;
import com.jcb.jcbbookingsystem.model.Vehicle;
import com.jcb.jcbbookingsystem.model.VehicleStatus;
import com.jcb.jcbbookingsystem.service.VehicleService;
import com.jcb.jcbbookingsystem.dto.MaintenanceRecordDTO;
import com.jcb.jcbbookingsystem.model.MaintenanceRecord;
import com.jcb.jcbbookingsystem.model.MaintenanceStatus;
import com.jcb.jcbbookingsystem.repository.MaintenanceRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final MaintenanceRecordRepository maintenanceRepository;
    private final VehicleService vehicleService;

    public List<MaintenanceRecordDTO> getAll() {
        return maintenanceRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<MaintenanceRecordDTO> getByVehicle(Long vehicleId) {
        return maintenanceRepository.findByVehicleId(vehicleId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public MaintenanceRecordDTO create(MaintenanceRecordDTO dto) {
        Vehicle vehicle = vehicleService.findEntity(dto.getVehicleId());

        MaintenanceRecord record = MaintenanceRecord.builder()
                .vehicle(vehicle)
                .serviceDate(dto.getServiceDate())
                .description(dto.getDescription())
                .cost(dto.getCost())
                .nextServiceDate(dto.getNextServiceDate())
                .status(dto.getStatus() != null ? dto.getStatus() : MaintenanceStatus.SCHEDULED)
                .performedBy(dto.getPerformedBy())
                .build();

        // Mark vehicle as under maintenance if service is starting now
        if (record.getStatus() == MaintenanceStatus.IN_PROGRESS) {
            vehicleService.updateStatus(vehicle.getId(), VehicleStatus.IN_MAINTENANCE);
        }

        return toDTO(maintenanceRepository.save(record));
    }

    public MaintenanceRecordDTO updateStatus(Long id, MaintenanceStatus status) {
        MaintenanceRecord record = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance record not found with id: " + id));

        record.setStatus(status);

        if (status == MaintenanceStatus.COMPLETED) {
            vehicleService.updateStatus(record.getVehicle().getId(), VehicleStatus.AVAILABLE);
        } else if (status == MaintenanceStatus.IN_PROGRESS) {
            vehicleService.updateStatus(record.getVehicle().getId(), VehicleStatus.IN_MAINTENANCE);
        }

        return toDTO(maintenanceRepository.save(record));
    }

    private MaintenanceRecordDTO toDTO(MaintenanceRecord r) {
        MaintenanceRecordDTO dto = new MaintenanceRecordDTO();
        dto.setId(r.getId());
        dto.setVehicleId(r.getVehicle().getId());
        dto.setServiceDate(r.getServiceDate());
        dto.setDescription(r.getDescription());
        dto.setCost(r.getCost());
        dto.setNextServiceDate(r.getNextServiceDate());
        dto.setStatus(r.getStatus());
        dto.setPerformedBy(r.getPerformedBy());
        return dto;
    }
}
