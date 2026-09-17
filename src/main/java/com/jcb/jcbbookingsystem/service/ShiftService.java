package com.jcb.jcbbookingsystem.service;

import com.jcb.jcbbookingsystem.dto.ShiftRequest;
import com.jcb.jcbbookingsystem.dto.ShiftResponse;
import com.jcb.jcbbookingsystem.dto.OvertimeResponse;
import com.jcb.jcbbookingsystem.dto.WorkloadResponse;
import com.jcb.jcbbookingsystem.exception.ConflictException;
import com.jcb.jcbbookingsystem.exception.ResourceNotFoundException;
import com.jcb.jcbbookingsystem.model.Driver;
import com.jcb.jcbbookingsystem.model.DriverStatus;
import com.jcb.jcbbookingsystem.model.Shift;
import com.jcb.jcbbookingsystem.model.ShiftStatus;
import com.jcb.jcbbookingsystem.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final DriverService driverService;

    public ShiftResponse createShift(ShiftRequest request) {

        Driver driver = driverService.findEntity(request.getDriverId());

        validateShift(request);

        if (driver.getStatus() == DriverStatus.INACTIVE ||
                driver.getStatus() == DriverStatus.ON_LEAVE) {

            throw new ConflictException(
                    "Cannot assign an inactive or on-leave driver"
            );
        }

        if (driver.getLicenseExpiryDate().isBefore(request.getDate())) {
            throw new ConflictException(
                    "Driver license is expired for the selected shift date"
            );
        }

        List<Shift> overlappingShifts =
                shiftRepository.findOverlappingShifts(
                        request.getDriverId(),
                        request.getDate(),
                        request.getStartTime(),
                        request.getEndTime()
                );

        if (!overlappingShifts.isEmpty()) {
            throw new ConflictException(
                    "Driver already has an overlapping shift"
            );
        }

        double existingHours = shiftRepository.findByDriverId(request.getDriverId())
                .stream()
                .filter(shift -> shift.getDate().equals(request.getDate()))
                .filter(shift -> shift.getStatus() != ShiftStatus.CANCELLED)
                .mapToDouble(shift ->
                        java.time.Duration.between(
                                shift.getStartTime(),
                                shift.getEndTime()
                        ).toMinutes() / 60.0
                )
                .sum();

        double newShiftHours =
                java.time.Duration.between(
                        request.getStartTime(),
                        request.getEndTime()
                ).toMinutes() / 60.0;

        if (existingHours + newShiftHours > 12.0) {
            throw new ConflictException(
                    "Driver workload exceeds the maximum 12 hours per day"
            );
        }

        Shift shift = Shift.builder()
                .driver(driver)
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(ShiftStatus.SCHEDULED)
                .build();

        Shift savedShift = shiftRepository.save(shift);

        return toResponse(savedShift);
    }

    public List<ShiftResponse> getAllShifts() {

        return shiftRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ShiftResponse getShiftById(Long id) {

        Shift shift = findEntity(id);

        return toResponse(shift);
    }

    public List<ShiftResponse> getShiftsByDriver(Long driverId) {

        driverService.findEntity(driverId);

        return shiftRepository.findByDriverId(driverId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ShiftResponse> getShiftsByDate(LocalDate date) {

        return shiftRepository.findByDate(date)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ShiftResponse> getShiftsByStatus(ShiftStatus status) {

        return shiftRepository.findByStatus(status)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ShiftResponse updateShift(
            Long id,
            ShiftRequest request) {

        Shift shift = findEntity(id);

        Driver driver = driverService.findEntity(request.getDriverId());

        validateShift(request);

        if (driver.getStatus() == DriverStatus.INACTIVE ||
                driver.getStatus() == DriverStatus.ON_LEAVE) {

            throw new ConflictException(
                    "Cannot assign an inactive or on-leave driver"
            );
        }

        if (driver.getLicenseExpiryDate().isBefore(request.getDate())) {
            throw new ConflictException(
                    "Driver license is expired for the selected shift date"
            );
        }

        List<Shift> overlappingShifts =
                shiftRepository.findOverlappingShifts(
                                request.getDriverId(),
                                request.getDate(),
                                request.getStartTime(),
                                request.getEndTime()
                        )
                        .stream()
                        .filter(existingShift -> !existingShift.getId().equals(id))
                        .collect(Collectors.toList());

        if (!overlappingShifts.isEmpty()) {
            throw new ConflictException(
                    "Driver already has an overlapping shift"
            );
        }

        double existingHours = shiftRepository.findByDriverId(request.getDriverId())
                .stream()
                .filter(existingShift -> existingShift.getDate().equals(request.getDate()))
                .filter(existingShift -> existingShift.getStatus() != ShiftStatus.CANCELLED)
                .filter(existingShift -> !existingShift.getId().equals(id))
                .mapToDouble(existingShift ->
                        java.time.Duration.between(
                                existingShift.getStartTime(),
                                existingShift.getEndTime()
                        ).toMinutes() / 60.0
                )
                .sum();

        double updatedShiftHours =
                java.time.Duration.between(
                        request.getStartTime(),
                        request.getEndTime()
                ).toMinutes() / 60.0;

        if (existingHours + updatedShiftHours > 12.0) {
            throw new ConflictException(
                    "Driver workload exceeds the maximum 12 hours per day"
            );
        }

        shift.setDriver(driver);
        shift.setDate(request.getDate());
        shift.setStartTime(request.getStartTime());
        shift.setEndTime(request.getEndTime());

        Shift updatedShift = shiftRepository.save(shift);

        return toResponse(updatedShift);
    }

    public ShiftResponse updateStatus(
            Long id,
            ShiftStatus status) {

        Shift shift = findEntity(id);

        shift.setStatus(status);

        Shift updatedShift = shiftRepository.save(shift);

        return toResponse(updatedShift);
    }

    public void deleteShift(Long id) {

        Shift shift = findEntity(id);

        shift.setStatus(ShiftStatus.CANCELLED);

        shiftRepository.save(shift);
    }

    public Shift findEntity(Long id) {

        return shiftRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Shift not found with id: " + id
                        )
                );
    }

    private void validateShift(ShiftRequest request) {

        if (request.getDriverId() == null) {
            throw new ConflictException(
                    "Driver ID is required"
            );
        }

        if (request.getDate() == null) {
            throw new ConflictException(
                    "Shift date is required"
            );
        }

        if (request.getStartTime() == null ||
                request.getEndTime() == null) {

            throw new ConflictException(
                    "Start time and end time are required"
            );
        }

        if (!request.getStartTime()
                .isBefore(request.getEndTime())) {

            throw new ConflictException(
                    "Start time must be before end time"
            );
        }

        if (request.getDate().isBefore(LocalDate.now())) {

            throw new ConflictException(
                    "Shift date cannot be in the past"
            );
        }
    }

    private ShiftResponse toResponse(Shift shift) {

        return ShiftResponse.builder()
                .id(shift.getId())
                .driverId(shift.getDriver().getId())
                .driverName(shift.getDriver().getFullName())
                .date(shift.getDate())
                .startTime(shift.getStartTime())
                .endTime(shift.getEndTime())
                .status(shift.getStatus())
                .build();
    }

    public OvertimeResponse getOvertime(Long driverId, LocalDate date) {

        driverService.findEntity(driverId);

        List<Shift> shifts = shiftRepository.findByDriverId(driverId)
                .stream()
                .filter(shift -> shift.getDate().equals(date))
                .filter(shift -> shift.getStatus() != ShiftStatus.CANCELLED)
                .collect(Collectors.toList());

        double totalHours = shifts.stream()
                .mapToDouble(shift ->
                        java.time.Duration.between(
                                shift.getStartTime(),
                                shift.getEndTime()
                        ).toMinutes() / 60.0
                )
                .sum();

        double normalHours = Math.min(totalHours, 8.0);

        double overtimeHours = Math.max(totalHours - 8.0, 0.0);

        return OvertimeResponse.builder()
                .driverId(driverId)
                .date(date)
                .totalHours(totalHours)
                .normalHours(normalHours)
                .overtimeHours(overtimeHours)
                .build();
    }

    public WorkloadResponse getWorkload(Long driverId, LocalDate date) {

        driverService.findEntity(driverId);

        List<Shift> shifts = shiftRepository.findByDriverId(driverId)
                .stream()
                .filter(shift -> shift.getDate().equals(date))
                .filter(shift -> shift.getStatus() != ShiftStatus.CANCELLED)
                .collect(Collectors.toList());

        double totalHours = shifts.stream()
                .mapToDouble(shift ->
                        java.time.Duration.between(
                                shift.getStartTime(),
                                shift.getEndTime()
                        ).toMinutes() / 60.0
                )
                .sum();

        double maximumHours = 12.0;

        double remainingHours = Math.max(maximumHours - totalHours, 0.0);

        boolean workloadExceeded = totalHours > maximumHours;

        return WorkloadResponse.builder()
                .driverId(driverId)
                .date(date)
                .totalHours(totalHours)
                .maximumHours(maximumHours)
                .remainingHours(remainingHours)
                .workloadExceeded(workloadExceeded)
                .build();
    }
}
