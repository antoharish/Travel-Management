package com.cts.project.Travelling_package.Controller;

import com.cts.project.Travelling_package.Model.Invoice;
import com.cts.project.Travelling_package.Service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/generate/{sessionId}")
    public Invoice generateInvoice(@PathVariable String sessionId) {
        return invoiceService.generateInvoice(sessionId);
    }

    @GetMapping("/{invoiceId}")
    public Invoice getInvoiceById(@PathVariable Long invoiceId) {
        return invoiceService.getInvoiceById(invoiceId);
    }
}