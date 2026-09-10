package com.jcb.jcbbookingsystem.repository;

import com.jcb.jcbbookingsystem.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByInvoiceId(Long invoiceId);
}
