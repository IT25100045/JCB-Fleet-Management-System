package com.jcb.jcbbookingsystem.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class InvoiceRequest {

    @NotNull(message = "Booking id is required")
    private Long bookingId;

    @NotNull(message = "User id is required")
    private Long userId;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    @NotNull(message = "Subtotal is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Subtotal cannot be negative")
    private BigDecimal subtotal;

    @DecimalMin(value = "0.0", inclusive = true, message = "Tax cannot be negative")
    private BigDecimal tax;
}

