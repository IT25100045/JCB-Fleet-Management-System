package com.jcb.jcbbookingsystem.service;

import com.jcb.jcbbookingsystem.dto.PaymentRequest;
import com.jcb.jcbbookingsystem.dto.PaymentResponse;
import com.jcb.jcbbookingsystem.exception.ConflictException;
import com.jcb.jcbbookingsystem.model.Invoice;
import com.jcb.jcbbookingsystem.model.InvoiceStatus;
import com.jcb.jcbbookingsystem.model.Payment;
import com.jcb.jcbbookingsystem.repository.InvoiceRepository;
import com.jcb.jcbbookingsystem.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceService invoiceService;

    public PaymentResponse recordPayment(PaymentRequest request) {
        Invoice invoice = invoiceService.findEntity(request.getInvoiceId());

        BigDecimal remaining = invoice.getTotalAmount().subtract(invoice.getAmountPaid());
        if (request.getAmount().compareTo(remaining) > 0) {
            throw new ConflictException("Payment amount exceeds remaining balance of " + remaining);
        }

        Payment payment = Payment.builder()
                .invoice(invoice)
                .amount(request.getAmount())
                .method(request.getMethod())
                .reference(request.getReference())
                .build();

        Payment saved = paymentRepository.save(payment);

        BigDecimal newAmountPaid = invoice.getAmountPaid().add(request.getAmount());
        invoice.setAmountPaid(newAmountPaid);

        if (newAmountPaid.compareTo(invoice.getTotalAmount()) >= 0) {
            invoice.setStatus(InvoiceStatus.PAID);
        } else {
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
        }
        invoiceRepository.save(invoice);

        return toResponse(saved);
    }

    public List<PaymentResponse> getByInvoice(Long invoiceId) {
        return paymentRepository.findByInvoiceId(invoiceId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public List<PaymentResponse> getAll() {
        return paymentRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    private PaymentResponse toResponse(Payment p) {
        return PaymentResponse.builder()
                .id(p.getId())
                .invoiceId(p.getInvoice().getId())
                .invoiceNumber(p.getInvoice().getInvoiceNumber())
                .amount(p.getAmount())
                .method(p.getMethod())
                .paidAt(p.getPaidAt())
                .reference(p.getReference())
                .build();
    }
}
