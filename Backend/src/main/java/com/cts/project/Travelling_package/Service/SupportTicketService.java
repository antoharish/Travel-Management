package com.cts.project.Travelling_package.Service;

import com.cts.project.Travelling_package.Exception.ResourceNotFoundException;
import com.cts.project.Travelling_package.Model.SupportTicket;


import com.cts.project.Travelling_package.Model.User;
import com.cts.project.Travelling_package.Repository.SupportTicketRepository;
import com.cts.project.Travelling_package.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


    @Service
    public class SupportTicketService {

        @Autowired
        private SupportTicketRepository supportTicketRepository;

        @Autowired
        private UserRepository userRepository;


        public List<SupportTicket> getAllTickets() {
            return supportTicketRepository.findAll();
        }

        public SupportTicket getTicketById(Long ticketID) {
            return supportTicketRepository.findById(ticketID)
                    .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        }

        public List<SupportTicket> getTicketsByUserId(int userId) {
            return supportTicketRepository.findByUser_UserId(userId);
        }


        public SupportTicket createTicket(SupportTicket ticket) {
            User user = userRepository.findById(ticket.getUser().getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            ticket.setUser(user); // Attach managed entity
            ticket.setStatus("OPEN");
            ticket.setAssignedAgent(null);

            return supportTicketRepository.save(ticket);
        }

         public SupportTicket assignTicket(Long ticketID, String agentName) {
             SupportTicket ticket = supportTicketRepository.findById(ticketID)
            .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

             ticket.setAssignedAgent(agentName.trim()); // Ensure it's stored as a plain text value
               ticket.setStatus("IN_PROGRESS");
              return supportTicketRepository.save(ticket);
}


        public SupportTicket resolveTicket(Long ticketID) {
            SupportTicket ticket = supportTicketRepository.findById(ticketID)
                    .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

            ticket.setStatus("RESOLVED");
            return supportTicketRepository.save(ticket);
        }

        public void deleteTicket(Long ticketID) {
            SupportTicket ticket = supportTicketRepository.findById(ticketID)
                    .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
            supportTicketRepository.delete(ticket);
        }
    }


