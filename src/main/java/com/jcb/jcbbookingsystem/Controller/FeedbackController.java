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
    public ResponseEntity<FeedbackResponse> create(Authentication auth, @Valid @RequestBody FeedbackRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(feedbackService.create(auth.getName(), request));
    }

    @GetMapping
    public ResponseEntity<List<FeedbackResponse>> getAll() {
        return ResponseEntity.ok(feedbackService.getAll());
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<FeedbackResponse>> getByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(feedbackService.getByBooking(bookingId));
    }
}
