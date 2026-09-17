package com.jcb.jcbbookingsystem.service;

import com.jcb.jcbbookingsystem.dto.FeedbackRequest;
import com.jcb.jcbbookingsystem.dto.FeedbackResponse;
import com.jcb.jcbbookingsystem.model.Feedback;
import com.jcb.jcbbookingsystem.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackResponse createFeedback(
            String operatorUsername,
            FeedbackRequest request) {

        Feedback feedback = Feedback.builder()
                .incidentId(request.getIncidentId())
                .operatorUsername(operatorUsername)
                .rating(request.getRating())
                .comments(request.getComments())
                .submittedAt(LocalDateTime.now())
                .build();

        Feedback saved = feedbackRepository.save(feedback);

        return toResponse(saved);
    }

    public List<FeedbackResponse> getMyFeedback(
            String operatorUsername) {

        return feedbackRepository
                .findByOperatorUsername(operatorUsername)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<FeedbackResponse> getFeedbackByIncident(
            Long incidentId) {

        return feedbackRepository
                .findByIncidentId(incidentId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<FeedbackResponse> getAllFeedback() {

        return feedbackRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private FeedbackResponse toResponse(Feedback feedback) {

        return FeedbackResponse.builder()
                .id(feedback.getId())
                .incidentId(feedback.getIncidentId())
                .operatorUsername(feedback.getOperatorUsername())
                .rating(feedback.getRating())
                .comments(feedback.getComments())
                .submittedAt(feedback.getSubmittedAt())
                .build();
    }
}