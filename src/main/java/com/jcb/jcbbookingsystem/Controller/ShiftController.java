package com.jcb.jcbbookingsystem.Controller;

import com.jcb.jcbbookingsystem.dto.ShiftRequest;
import com.jcb.jcbbookingsystem.dto.ShiftResponse;
import com.jcb.jcbbookingsystem.model.ShiftStatus;
import com.jcb.jcbbookingsystem.service.ShiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    @PostMapping
    public ResponseEntity<ShiftResponse> assign(@Valid @RequestBody ShiftRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shiftService.assignShift(request));
    }

    @GetMapping
    public ResponseEntity<List<ShiftResponse>> getAll() {
        return ResponseEntity.ok(shiftService.getAll());
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<ShiftResponse>> getByDriver(@PathVariable Long driverId) {
        return ResponseEntity.ok(shiftService.getByDriver(driverId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ShiftResponse> updateStatus(@PathVariable Long id, @RequestParam ShiftStatus status) {
        return ResponseEntity.ok(shiftService.updateStatus(id, status));
    }
}

