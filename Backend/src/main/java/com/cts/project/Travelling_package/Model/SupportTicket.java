package com.cts.project.Travelling_package.Model;


import jakarta.persistence.*;
import lombok.Data;

    @Data
    @Entity
    @Table(name = "support_ticket")
    public class SupportTicket {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "ticket_id")
        private Long ticketID;

//        @Column(name = "user_id", nullable = false)
//        private Long userID;

        @Column(name = "issue", nullable = false)
        private String issue;

        @Column(name = "status", nullable = false)
        private String status; // "OPEN", "IN_PROGRESS", "RESOLVED"

        @Column(name = "assigned_agent")
        private String assignedAgent;

        @ManyToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "userId", nullable = false)
        private User user;

 }


