package com.cts.project.Travelling_package.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "emailId"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_id")
    private int userId;

    private String username;

    @Column(name = "emailId", nullable = false)
    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

    @Column(name = "password", nullable = false)
    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "contact_number", nullable = false)
    @NotNull(message = "Contact number cannot be null")
    private String contactNumber;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Payment> payments;
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL , orphanRemoval = true)
//    @JsonIgnore
//    private List<Review> reviews;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL , orphanRemoval = true)
//    @JsonIgnore
//    private List<SupportTicket> supportTickets;

}

