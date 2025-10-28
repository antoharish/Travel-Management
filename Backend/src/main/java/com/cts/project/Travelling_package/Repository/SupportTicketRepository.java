package com.cts.project.Travelling_package.Repository;


import com.cts.project.Travelling_package.Model.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {

    List<SupportTicket> findByUser_UserId(int user);

    List<SupportTicket> findByStatus(String status);
}

