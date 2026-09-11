package com.jcb.jcbbookingsystem.service;

import com.jcb.jcbbookingsystem.model.User;
import com.jcb.jcbbookingsystem.repository.UserRepository;
import com.jcb.jcbbookingsystem.dto.BookingRequest;
import com.jcb.jcbbookingsystem.dto.BookingResponse;
import com.jcb.jcbbookingsystem.model.Booking;
import com.jcb.jcbbookingsystem.model.BookingStatus;
import com.jcb.jcbbookingsystem.repository.BookingRepository;
import com.jcb.jcbbookingsystem.exception.ConflictException;
import com.jcb.jcbbookingsystem.exception.ResourceNotFoundException;
import com.jcb.jcbbookingsystem.model.Vehicle;
import com.jcb.jcbbookingsystem.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final VehicleService vehicleService;
    private final UserRepository userRepository;

    public BookingResponse createBooking(String username, BookingRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Vehicle vehicle = vehicleService.findEntity(request.getVehicleId());

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new ConflictException("End date cannot be before start date");
        }

        List<Booking> overlapping = bookingRepository.findOverlappingBookings(
                vehicle.getId(), request.getStartDate(), request.getEndDate());

        if (!overlapping.isEmpty()) {
            throw new ConflictException("Vehicle is already booked for the selected dates");
        }

        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
        BigDecimal totalPrice = vehicle.getPricePerDay().multiply(BigDecimal.valueOf(days));

        Booking booking = Booking.builder()
                .user(user)
                .vehicle(vehicle)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .totalPrice(totalPrice)
                .status(BookingStatus.PENDING)
                .build();

        Booking saved = bookingRepository.save(booking);
        return toResponse(saved);
    }

    public List<BookingResponse> getMyBookings(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        return bookingRepository.findByUserId(user.getId()).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public BookingResponse updateStatus(Long bookingId, BookingStatus status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
        booking.setStatus(status);
        return toResponse(bookingRepository.save(booking));
    }

    public void cancelBooking(String username, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (!booking.getUser().getUsername().equals(username)) {
            throw new ResourceNotFoundException("Booking not found with id: " + bookingId);
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    public Booking findEntity(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }

    private BookingResponse toResponse(Booking b) {
        return BookingResponse.builder()
                .id(b.getId())
                .userId(b.getUser().getId())
                .username(b.getUser().getUsername())
                .vehicleId(b.getVehicle().getId())
                .vehicleName(b.getVehicle().getName())
                .startDate(b.getStartDate())
                .endDate(b.getEndDate())
                .totalPrice(b.getTotalPrice())
                .status(b.getStatus())
                .createdAt(b.getCreatedAt())
                .build();
    }
}
