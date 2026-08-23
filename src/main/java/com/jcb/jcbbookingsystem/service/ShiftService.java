package com.jcb.jcbbookingsystem.service;

import com.jcb.jcbbookingsystem.dto.ShiftRequest;
import com.jcb.jcbbookingsystem.dto.ShiftResponse;
import com.jcb.jcbbookingsystem.model.Driver;
import com.jcb.jcbbookingsystem.model.Shift;
import com.jcb.jcbbookingsystem.model.ShiftStatus;
import com.jcb.jcbbookingsystem.repository.ShiftRepository;
import com.jcb.jcbbookingsystem.exception.ConflictException;
import com.jcb.jcbbookingsystem.exception.ResourceNotFoundException;
import com.jcb.jcbbookingsystem.model.Vehicle;
import com.jcb.jcbbookingsystem.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final DriverService driverService;
    private final VehicleService vehicleService;

    public ShiftResponse assignShift(ShiftRequest request) {
        Driver driver = driverService.findEntity(request.getDriverId());
        Vehicle vehicle = vehicleService.findEntity(request.getVehicleId());

        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new ConflictException("End time cannot be before start time");
        }

        List<Shift> overlapping = shiftRepository.findOverlappingShiftsForDriver(
                driver.getId(), request.getStartTime(), request.getEndTime());

        if (!overlapping.isEmpty()) {
            throw new ConflictException("Driver already has a shift during this time");
        }

        Shift shift = Shift.builder()
                .driver(driver)
                .vehicle(vehicle)
                .bookingId(request.getBookingId())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(ShiftStatus.SCHEDULED)
                .notes(request.getNotes())
                .build();

        return toResponse(shiftRepository.save(shift));
    }

    public List<ShiftResponse> getByDriver(Long driverId) {
        return shiftRepository.findByDriverId(driverId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public List<ShiftResponse> getAll() {
        return shiftRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ShiftResponse updateStatus(Long shiftId, ShiftStatus status) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with id: " + shiftId));
        shift.setStatus(status);
        return toResponse(shiftRepository.save(shift));
    }

    private ShiftResponse toResponse(Shift s) {
        return ShiftResponse.builder()
                .id(s.getId())
                .driverId(s.getDriver().getId())
                .driverName(s.getDriver().getFullName())
                .vehicleId(s.getVehicle().getId())
                .vehicleName(s.getVehicle().getName())
                .bookingId(s.getBookingId())
                .startTime(s.getStartTime())
                .endTime(s.getEndTime())
                .status(s.getStatus())
                .notes(s.getNotes())
                .build();
    }
}
