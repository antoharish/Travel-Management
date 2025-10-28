package com.cts.project.Travelling_package.Controller;



import com.cts.project.Travelling_package.Model.SupportTicket;
import com.cts.project.Travelling_package.Model.User;
import com.cts.project.Travelling_package.Repository.UserRepository;
import com.cts.project.Travelling_package.Service.SupportTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
public class SupportTicketController {

    @Autowired
    private SupportTicketService supportTicketService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<SupportTicket> getAllTickets() {
        return supportTicketService.getAllTickets();
    }


    @GetMapping("/{id}")
    public ResponseEntity<SupportTicket> getTicketById(@PathVariable Long id) {
        SupportTicket ticket = supportTicketService.getTicketById(id);
        return ResponseEntity.ok(ticket);
    }


    @GetMapping("/user/{userId}")
    public List<SupportTicket> getTicketsByUserId(@PathVariable int userId) {
        return supportTicketService.getTicketsByUserId(userId);
    }

    @PostMapping
    public SupportTicket createTicket(@RequestBody SupportTicket ticket) {
        return supportTicketService.createTicket(ticket);
    }

//    @PutMapping("/{id}/assign")
//    public ResponseEntity<SupportTicket> assignTicket(@PathVariable Long id, @RequestBody String agentName) {
//        SupportTicket ticket = supportTicketService.assignTicket(id, agentName);
//        return ResponseEntity.ok(ticket);
//    }
      @PutMapping("/{id}/assign")
     public ResponseEntity<SupportTicket> assignTicket(@PathVariable Long id, @RequestBody Map<String, Object> request) {
           String agentName = request.get("assignedAgent").toString(); // Extract `assignedAgent` from JSON
            SupportTicket ticket = supportTicketService.assignTicket(id, agentName);
            return ResponseEntity.ok(ticket);
}

    @PutMapping("/{id}/resolve")
    public ResponseEntity<SupportTicket> resolveTicket(@PathVariable Long id) {
        SupportTicket ticket = supportTicketService.resolveTicket(id);
        return ResponseEntity.ok(ticket);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        supportTicketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}
