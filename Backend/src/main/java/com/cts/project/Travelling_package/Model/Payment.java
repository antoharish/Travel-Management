package com.cts.project.Travelling_package.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import lombok.Data;

import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Payment_Id")
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "User_id", nullable = false)
    private User user;

    @Column(name = "Booking_Id")
    private Long bookingId;

    @Column(name = "Amount", nullable = false)
    private Long amount;

    @Column(name = "Status", nullable = false)
    private String status;

    @Column(name = "Payment_Method", nullable = false)
    private String paymentMethod;

    @Column(name = "Session_Id", nullable = false)
    private String sessionId;

    @Column(name = "Session_Url", length = 2048, nullable = false)
    private String sessionUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "Payment_Type", nullable = false)
    private PaymentType paymentType;

}
