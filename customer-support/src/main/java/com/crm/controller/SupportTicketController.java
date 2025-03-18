package com.crm.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crm.dto.SupportTicketDTO;
import com.crm.enums.Status;

import java.util.List;

@RestController
@RequestMapping("/api/support")
public interface SupportTicketController {

    @GetMapping
    ResponseEntity<List<SupportTicketDTO>> retrieveAllSupportTickets();

    @PostMapping("/tickets")
    ResponseEntity<SupportTicketDTO> createSupportTicket(@Valid @RequestBody SupportTicketDTO supportTicketDto);

    @GetMapping("/{ticketId}")
    ResponseEntity<SupportTicketDTO> getSupportTicketById(@PathVariable("ticketId") Long ticketId);

    @GetMapping("/customer/{customerId}")
    ResponseEntity<List<SupportTicketDTO>> getSupportTicketsByCustomer(@PathVariable Long customerId);

    @GetMapping("/status/{status}")
    ResponseEntity<List<SupportTicketDTO>> getSupportTicketsByStatus(@PathVariable Status status);

    @PutMapping("/{ticketId}/{status}")
    ResponseEntity<SupportTicketDTO> updateTicketStatus(@PathVariable("ticketId") Long ticketId, @PathVariable("status") Status status);

    @PutMapping("/{ticketId}/assign")
    ResponseEntity<SupportTicketDTO> assignTicketToAgent(@PathVariable Long ticketId, @RequestParam Long agentId);

    @DeleteMapping("/{ticketId}")
    ResponseEntity<String> deleteSupportTicketById(@PathVariable Long ticketId);

}
