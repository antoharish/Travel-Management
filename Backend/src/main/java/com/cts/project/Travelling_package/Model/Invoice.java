package com.cts.project.Travelling_package.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Invoice_Id")
    private Long invoiceId;


    @ManyToOne
    @JoinColumn(name = "User_Id", nullable = false)
    private User user;

    @Column(name = "Total_Amount", nullable = false)
    private Long totalAmount;

    @Column(name = "Timestamp", nullable = false)
    private LocalDateTime timestamp;
}