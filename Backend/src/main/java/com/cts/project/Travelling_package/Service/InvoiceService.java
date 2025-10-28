package com.cts.project.Travelling_package.Service;

import com.cts.project.Travelling_package.Model.Invoice;
import com.cts.project.Travelling_package.Model.Payment;
import com.cts.project.Travelling_package.Model.User;
import com.cts.project.Travelling_package.Repository.InvoiceRepository;
import com.cts.project.Travelling_package.Repository.PaymentRepository;
import com.cts.project.Travelling_package.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    public Invoice generateInvoice(String sessionId) {

        Payment payment = paymentRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("Payment not found with session ID: " + sessionId));


        Invoice invoice = new Invoice();
//        invoice.setBookingId(payment.getBookingId());
        invoice.setUser(payment.getUser());
        invoice.setTotalAmount(payment.getAmount());
        invoice.setTimestamp(LocalDateTime.now());


        return invoiceRepository.save(invoice);
    }

    public Invoice getInvoiceById(Long invoiceId) {
        return invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with ID: " + invoiceId));
    }
}

