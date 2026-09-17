package com.jcb.jcbbookingsystem.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackResponse {

    private Long id;
    private Long incidentId;
    private String operatorUsername;
    private Integer rating;
    private String comments;
    private LocalDateTime submittedAt;
}