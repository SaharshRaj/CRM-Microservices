package com.crm.controller;

//import com.crm.dto.SupportTicketDTO;
import com.crm.entities.SupportTicket;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("api/support")
public interface SupportTicketController {

//    @GetMapping("")
//    public ResponseEntity<List<SupportTicketDTO>> getAllSupportTickets();
	
	@PostMapping("/tickets")
    ResponseEntity<SupportTicket> createTicket(@RequestBody SupportTicket supportTicket);

    @GetMapping("/tickets/{id}")
    ResponseEntity<SupportTicket> getTicketById(@PathVariable Long id);

    @GetMapping("/tickets")
    ResponseEntity<List<SupportTicket>> getAllTickets();

    @PutMapping("/tickets/{id}")
    ResponseEntity<SupportTicket> updateTicket(@PathVariable Long id, @RequestBody SupportTicket supportTicket);

    @DeleteMapping("/tickets/{id}")
    ResponseEntity<Void> deleteTicket(@PathVariable Long id);
	
}
