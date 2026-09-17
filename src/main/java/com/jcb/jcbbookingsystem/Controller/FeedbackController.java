package com.jcb.jcbbookingsystem.Controller;

import com.jcb.jcbbookingsystem.dto.FeedbackRequest;
import com.jcb.jcbbookingsystem.dto.FeedbackResponse;
import com.jcb.jcbbookingsystem.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackResponse> createFeedback(
            Authentication auth,
            @Valid @RequestBody FeedbackRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(feedbackService.createFeedback(
                        auth.getName(), request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<FeedbackResponse>> getMyFeedback(
            Authentication auth) {

        return ResponseEntity.ok(
                feedbackService.getMyFeedback(auth.getName()));
    }

    @GetMapping("/incident/{incidentId}")
    public ResponseEntity<List<FeedbackResponse>> getFeedbackByIncident(
            @PathVariable Long incidentId) {

        return ResponseEntity.ok(
                feedbackService.getFeedbackByIncident(incidentId));
    }

    @GetMapping
    public ResponseEntity<List<FeedbackResponse>> getAllFeedback() {

        return ResponseEntity.ok(
                feedbackService.getAllFeedback());
    }
}