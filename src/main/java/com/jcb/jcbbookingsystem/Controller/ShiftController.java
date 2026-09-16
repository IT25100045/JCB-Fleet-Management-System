package com.jcb.jcbbookingsystem.Controller;

import com.jcb.jcbbookingsystem.dto.ShiftRequest;
import com.jcb.jcbbookingsystem.dto.ShiftResponse;
import com.jcb.jcbbookingsystem.dto.OvertimeResponse;
import com.jcb.jcbbookingsystem.dto.WorkloadResponse;
import com.jcb.jcbbookingsystem.model.ShiftStatus;
import com.jcb.jcbbookingsystem.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
public class ShiftController {
    private final ShiftService shiftService;

    @PostMapping
    public ResponseEntity<ShiftResponse> createShift(
            @RequestBody ShiftRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(shiftService.createShift(request));
    }

    @GetMapping
    public ResponseEntity<List<ShiftResponse>> getAllShifts() {

        return ResponseEntity.ok(
                shiftService.getAllShifts()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShiftResponse> getShiftById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                shiftService.getShiftById(id)
        );
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<ShiftResponse>> getShiftsByDriver(
            @PathVariable Long driverId) {

        return ResponseEntity.ok(
                shiftService.getShiftsByDriver(driverId)
        );
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<ShiftResponse>> getShiftsByDate(
            @PathVariable LocalDate date) {

        return ResponseEntity.ok(
                shiftService.getShiftsByDate(date)
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ShiftResponse>> getShiftsByStatus(
            @PathVariable ShiftStatus status) {

        return ResponseEntity.ok(
                shiftService.getShiftsByStatus(status)
        );
    }

    @GetMapping("/driver/{driverId}/overtime")
    public ResponseEntity<OvertimeResponse> getOvertime(
            @PathVariable Long driverId,
            @RequestParam LocalDate date) {

        return ResponseEntity.ok(
                shiftService.getOvertime(driverId, date)
        );
    }

    @GetMapping("/driver/{driverId}/workload")
    public ResponseEntity<WorkloadResponse> getWorkload(
            @PathVariable Long driverId,
            @RequestParam LocalDate date) {

        return ResponseEntity.ok(
                shiftService.getWorkload(driverId, date)
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<ShiftResponse> updateShift(
            @PathVariable Long id,
            @RequestBody ShiftRequest request) {

        return ResponseEntity.ok(
                shiftService.updateShift(id, request)
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ShiftResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam ShiftStatus status) {

        return ResponseEntity.ok(
                shiftService.updateStatus(id, status)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShift(
            @PathVariable Long id) {

        shiftService.deleteShift(id);

        return ResponseEntity.noContent().build();
    }
}

