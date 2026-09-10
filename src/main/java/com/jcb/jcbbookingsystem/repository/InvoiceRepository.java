package com.jcb.jcbbookingsystem.repository;

import com.jcb.jcbbookingsystem.model.Invoice;
import com.jcb.jcbbookingsystem.model.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByUserId(Long userId);
    List<Invoice> findByStatus(InvoiceStatus status);
    Optional<Invoice> findByBookingId(Long bookingId);
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
}
