package com.jcb.jcbbookingsystem.service;

import com.jcb.jcbbookingsystem.dto.InvoiceRequest;
import com.jcb.jcbbookingsystem.dto.InvoiceResponse;
import com.jcb.jcbbookingsystem.exception.ResourceNotFoundException;
import com.jcb.jcbbookingsystem.model.Invoice;
import com.jcb.jcbbookingsystem.model.InvoiceStatus;
import com.jcb.jcbbookingsystem.model.User;
import com.jcb.jcbbookingsystem.repository.InvoiceRepository;
import com.jcb.jcbbookingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;

    public InvoiceResponse createInvoice(InvoiceRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        BigDecimal tax = request.getTax() != null ? request.getTax() : BigDecimal.ZERO;
        BigDecimal total = request.getSubtotal().add(tax);

        Invoice invoice = Invoice.builder()
                .invoiceNumber(generateInvoiceNumber())
                .bookingId(request.getBookingId())
                .user(user)
                .issueDate(LocalDate.now())
                .dueDate(request.getDueDate())
                .subtotal(request.getSubtotal())
                .tax(tax)
                .totalAmount(total)
                .amountPaid(BigDecimal.ZERO)
                .status(InvoiceStatus.UNPAID)
                .build();

        return toResponse(invoiceRepository.save(invoice));
    }

    public List<InvoiceResponse> getAll() {
        return invoiceRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<InvoiceResponse> getByUser(Long userId) {
        return invoiceRepository.findByUserId(userId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public InvoiceResponse getById(Long id) {
        return toResponse(findEntity(id));
    }

    public Invoice findEntity(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
    }

    private String generateInvoiceNumber() {
        return "INV-" + System.currentTimeMillis();
    }

    private InvoiceResponse toResponse(Invoice inv) {
        return InvoiceResponse.builder()
                .id(inv.getId())
                .invoiceNumber(inv.getInvoiceNumber())
                .bookingId(inv.getBookingId())
                .userId(inv.getUser().getId())
                .username(inv.getUser().getUsername())
                .issueDate(inv.getIssueDate())
                .dueDate(inv.getDueDate())
                .subtotal(inv.getSubtotal())
                .tax(inv.getTax())
                .totalAmount(inv.getTotalAmount())
                .amountPaid(inv.getAmountPaid())
                .status(inv.getStatus())
                .createdAt(inv.getCreatedAt())
                .build();
    }
}
