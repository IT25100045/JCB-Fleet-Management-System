package com.jcb.jcbbookingsystem.service;

import com.jcb.jcbbookingsystem.dto.FeedbackRequest;
import com.jcb.jcbbookingsystem.dto.FeedbackResponse;
import com.jcb.jcbbookingsystem.exception.ResourceNotFoundException;
import com.jcb.jcbbookingsystem.model.Feedback;
import com.jcb.jcbbookingsystem.model.User;
import com.jcb.jcbbookingsystem.repository.FeedbackRepository;
import com.jcb.jcbbookingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    public FeedbackResponse create(String username, FeedbackRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Feedback feedback = Feedback.builder()
                .bookingId(request.getBookingId())
                .user(user)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        return toResponse(feedbackRepository.save(feedback));
    }

    public List<FeedbackResponse> getAll() {
        return feedbackRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<FeedbackResponse> getByBooking(Long bookingId) {
        return feedbackRepository.findByBookingId(bookingId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    private FeedbackResponse toResponse(Feedback f) {
        return FeedbackResponse.builder()
                .id(f.getId())
                .bookingId(f.getBookingId())
                .userId(f.getUser().getId())
                .username(f.getUser().getUsername())
                .rating(f.getRating())
                .comment(f.getComment())
                .createdAt(f.getCreatedAt())
                .build();
    }
}

