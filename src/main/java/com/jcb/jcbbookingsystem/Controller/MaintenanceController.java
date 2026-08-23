package com.jcb.jcbbookingsystem.Controller;

import com.jcb.jcbbookingsystem.dto.MaintenanceRecordDTO;
import com.jcb.jcbbookingsystem.model.MaintenanceStatus;
import com.jcb.jcbbookingsystem.service.MaintenanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @GetMapping
    public ResponseEntity<List<MaintenanceRecordDTO>> getAll() {
        return ResponseEntity.ok(maintenanceService.getAll());
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<MaintenanceRecordDTO>> getByVehicle(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(maintenanceService.getByVehicle(vehicleId));
    }

    @PostMapping
    public ResponseEntity<MaintenanceRecordDTO> create(@Valid @RequestBody MaintenanceRecordDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(maintenanceService.create(dto));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<MaintenanceRecordDTO> updateStatus(@PathVariable Long id, @RequestParam MaintenanceStatus status) {
        return ResponseEntity.ok(maintenanceService.updateStatus(id, status));
    }
}

