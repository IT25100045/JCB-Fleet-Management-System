package com.jcb.jcbbookingsystem.dto;

import com.jcb.jcbbookingsystem.model.IncidentSeverity;
import com.jcb.jcbbookingsystem.model.IncidentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentReportResponse {

    private Long id;
    private Long jcbId;
    private String operatorUsername;
    private String title;
    private String description;
    private IncidentSeverity severity;
    private IncidentStatus status;
    private String photoUrl;
    private LocalDateTime reportedAt;
    private LocalDateTime resolvedAt;
}


