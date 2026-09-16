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
    public ResponseEntity<IncidentReportResponse> createReport(
            Authentication auth,
            @Valid @RequestBody IncidentReportRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(incidentReportService.createReport(
                        auth.getName(), request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<IncidentReportResponse>> getMyReports(
            Authentication auth) {

        return ResponseEntity.ok(
                incidentReportService.getMyReports(auth.getName()));
    }

    @GetMapping
    public ResponseEntity<List<IncidentReportResponse>> getAllReports() {

        return ResponseEntity.ok(
                incidentReportService.getAllReports());
    }

    @GetMapping("/jcb/{jcbId}")
    public ResponseEntity<List<IncidentReportResponse>> getReportsByJcb(
            @PathVariable Long jcbId) {

        return ResponseEntity.ok(
                incidentReportService.getReportsByJcb(jcbId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<IncidentReportResponse>> getReportsByStatus(
            @PathVariable IncidentStatus status) {

        return ResponseEntity.ok(
                incidentReportService.getReportsByStatus(status));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<IncidentReportResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam IncidentStatus status) {

        return ResponseEntity.ok(
                incidentReportService.updateStatus(id, status));
    }
}