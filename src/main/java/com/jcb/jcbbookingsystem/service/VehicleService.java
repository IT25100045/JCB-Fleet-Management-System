package com.jcb.jcbbookingsystem.service;

import com.jcb.jcbbookingsystem.model.Vehicle;
import com.jcb.jcbbookingsystem.repository.VehicleRepository;
import com.jcb.jcbbookingsystem.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
    }

    public Vehicle findEntity(Long id) {
        return getVehicleById(id);
    }

    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Vehicle updateVehicle(Long id, Vehicle updatedVehicle) {
        Vehicle vehicle = getVehicleById(id);
        vehicle.setName(updatedVehicle.getName());
        vehicle.setVehicleNumber(updatedVehicle.getVehicleNumber());
        vehicle.setVehicleType(updatedVehicle.getVehicleType());
        vehicle.setPricePerDay(updatedVehicle.getPricePerDay());
        vehicle.setStatus(updatedVehicle.getStatus());
        vehicle.setHealthPercent(updatedVehicle.getHealthPercent());
        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Long id) {
        Vehicle vehicle = getVehicleById(id);
        vehicleRepository.delete(vehicle);
    }
}