package com.jcb.jcbbookingsystem.Controller;

import com.jcb.jcbbookingsystem.dto.IncidentReportRequest;
import com.jcb.jcbbookingsystem.dto.IncidentReportResponse;
import com.jcb.jcbbookingsystem.model.IncidentStatus;
import com.jcb.jcbbookingsystem.service.IncidentReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class IncidentReportController {

    private final IncidentReportService incidentReportService;

    @PostMapping
    public ResponseEntity<IncidentReportResponse> create(Authentication auth, @Valid @RequestBody IncidentReportRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(incidentReportService.create(auth.getName(), request));
    }

    @GetMapping
    public ResponseEntity<List<IncidentReportResponse>> getAll() {
        return ResponseEntity.ok(incidentReportService.getAll());
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<IncidentReportResponse>> getByVehicle(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(incidentReportService.getByVehicle(vehicleId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<IncidentReportResponse> updateStatus(@PathVariable Long id, @RequestParam IncidentStatus status) {
        return ResponseEntity.ok(incidentReportService.updateStatus(id, status));
    }
}

