package com.cts.project.Travelling_package.Repository;

import com.cts.project.Travelling_package.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUser_UserId(Long userId);
    Optional<Payment> findBySessionId(String sessionId);
}