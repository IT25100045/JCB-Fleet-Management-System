package com.jcb.jcbbookingsystem.service;

import com.jcb.jcbbookingsystem.model.Vehicle;
import com.jcb.jcbbookingsystem.service.VehicleService;
import com.jcb.jcbbookingsystem.dto.FuelRecordDTO;
import com.jcb.jcbbookingsystem.model.FuelRecord;
import com.jcb.jcbbookingsystem.repository.FuelRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FuelService {

    private final FuelRecordRepository fuelRecordRepository;
    private final VehicleService vehicleService;

    public List<FuelRecordDTO> getAll() {
        return fuelRecordRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<FuelRecordDTO> getByVehicle(Long vehicleId) {
        return fuelRecordRepository.findByVehicleId(vehicleId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public FuelRecordDTO create(FuelRecordDTO dto) {
        Vehicle vehicle = vehicleService.findEntity(dto.getVehicleId());

        FuelRecord record = FuelRecord.builder()
                .vehicle(vehicle)
                .fuelDate(dto.getFuelDate())
                .liters(dto.getLiters())
                .cost(dto.getCost())
                .odometerReading(dto.getOdometerReading())
                .build();

        FuelRecord saved = fuelRecordRepository.save(record);

        // Keep the vehicle's odometer reading up to date
        if (dto.getOdometerReading() != null) {
            vehicle.setCurrentOdometer(dto.getOdometerReading());
        }

        return toDTO(saved);
    }

    private FuelRecordDTO toDTO(FuelRecord r) {
        FuelRecordDTO dto = new FuelRecordDTO();
        dto.setId(r.getId());
        dto.setVehicleId(r.getVehicle().getId());
        dto.setFuelDate(r.getFuelDate());
        dto.setLiters(r.getLiters());
        dto.setCost(r.getCost());
        dto.setOdometerReading(r.getOdometerReading());
        return dto;
    }
}

