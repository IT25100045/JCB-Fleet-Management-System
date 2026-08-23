package com.jcb.jcbbookingsystem.service;

import com.jcb.jcbbookingsystem.dto.IncidentReportRequest;
import com.jcb.jcbbookingsystem.dto.IncidentReportResponse;
import com.jcb.jcbbookingsystem.exception.ResourceNotFoundException;
import com.jcb.jcbbookingsystem.model.IncidentReport;
import com.jcb.jcbbookingsystem.model.IncidentSeverity;
import com.jcb.jcbbookingsystem.model.IncidentStatus;
import com.jcb.jcbbookingsystem.model.User;
import com.jcb.jcbbookingsystem.repository.IncidentReportRepository;
import com.jcb.jcbbookingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncidentReportService {

    private final IncidentReportRepository incidentReportRepository;
    private final UserRepository userRepository;

    public IncidentReportResponse create(String username, IncidentReportRequest request) {
        User reporter = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        IncidentReport report = IncidentReport.builder()
                .vehicleId(request.getVehicleId())
                .bookingId(request.getBookingId())
                .reportedBy(reporter)
                .description(request.getDescription())
                .incidentDate(request.getIncidentDate())
                .severity(request.getSeverity() != null ? request.getSeverity() : IncidentSeverity.LOW)
                .status(IncidentStatus.REPORTED)
                .build();

        return toResponse(incidentReportRepository.save(report));
    }

    public List<IncidentReportResponse> getAll() {
        return incidentReportRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<IncidentReportResponse> getByVehicle(Long vehicleId) {
        return incidentReportRepository.findByVehicleId(vehicleId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public IncidentReportResponse updateStatus(Long id, IncidentStatus status) {
        IncidentReport report = incidentReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incident report not found with id: " + id));
        report.setStatus(status);
        return toResponse(incidentReportRepository.save(report));
    }

    private IncidentReportResponse toResponse(IncidentReport r) {
        return IncidentReportResponse.builder()
                .id(r.getId())
                .vehicleId(r.getVehicleId())
                .bookingId(r.getBookingId())
                .reportedById(r.getReportedBy().getId())
                .reportedByUsername(r.getReportedBy().getUsername())
                .description(r.getDescription())
                .incidentDate(r.getIncidentDate())
                .severity(r.getSeverity())
                .status(r.getStatus())
                .createdAt(r.getCreatedAt())
                .build();
    }
}
