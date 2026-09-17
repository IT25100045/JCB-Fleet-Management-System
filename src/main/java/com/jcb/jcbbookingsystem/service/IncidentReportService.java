package com.jcb.jcbbookingsystem.service;

import com.jcb.jcbbookingsystem.dto.IncidentReportRequest;
import com.jcb.jcbbookingsystem.dto.IncidentReportResponse;
import com.jcb.jcbbookingsystem.model.IncidentReport;
import com.jcb.jcbbookingsystem.model.IncidentStatus;
import com.jcb.jcbbookingsystem.repository.IncidentReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncidentReportService {

    private final IncidentReportRepository incidentReportRepository;

    public IncidentReportResponse createReport(
            String operatorUsername,
            IncidentReportRequest request) {

        IncidentReport report = IncidentReport.builder()
                .jcbId(request.getJcbId())
                .operatorUsername(operatorUsername)
                .title(request.getTitle())
                .description(request.getDescription())
                .severity(request.getSeverity())
                .photoUrl(request.getPhotoUrl())
                .status(IncidentStatus.OPEN)
                .reportedAt(LocalDateTime.now())
                .build();

        IncidentReport saved = incidentReportRepository.save(report);

        return toResponse(saved);
    }

    public List<IncidentReportResponse> getMyReports(
            String operatorUsername) {

        return incidentReportRepository
                .findByOperatorUsername(operatorUsername)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<IncidentReportResponse> getAllReports() {

        return incidentReportRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<IncidentReportResponse> getReportsByJcb(
            Long jcbId) {

        return incidentReportRepository.findByJcbId(jcbId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<IncidentReportResponse> getReportsByStatus(
            IncidentStatus status) {

        return incidentReportRepository.findByStatus(status)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public IncidentReportResponse updateStatus(
            Long id,
            IncidentStatus status) {

        IncidentReport report = incidentReportRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Incident report not found with id: " + id));

        report.setStatus(status);

        if (status == IncidentStatus.RESOLVED) {
            report.setResolvedAt(LocalDateTime.now());
        }

        return toResponse(
                incidentReportRepository.save(report));
    }

    private IncidentReportResponse toResponse(
            IncidentReport report) {

        return IncidentReportResponse.builder()
                .id(report.getId())
                .jcbId(report.getJcbId())
                .operatorUsername(report.getOperatorUsername())
                .title(report.getTitle())
                .description(report.getDescription())
                .severity(report.getSeverity())
                .status(report.getStatus())
                .photoUrl(report.getPhotoUrl())
                .reportedAt(report.getReportedAt())
                .resolvedAt(report.getResolvedAt())
                .build();
    }
}