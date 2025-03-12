package com.crm.controller;

//import com.crm.dto.SupportTicketDTO;
import com.crm.entities.SupportTicket;
//import com.crm.service.SupportTicketService;
import com.crm.service.SupportTicketServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SupportTicketControllerImpl implements SupportTicketController {
    
    @Autowired
    private SupportTicketServiceImpl supportTicketService;
    
    /**
     * Add JavaDoc
//     */
//    @Override
//    public ResponseEntity<List<SupportTicketDTO>> getAllSupportTickets() {
//        List<SupportTicketDTO> customerProfileDTOS = service.retrieveAllProfiles();
//        return new ResponseEntity<>(customerProfileDTOS, HttpStatus.OK);
//    }

	@Override
	public ResponseEntity<SupportTicket> createTicket(@RequestBody SupportTicket supportTicket) {
		SupportTicket createdTicket = supportTicketService.createTicket(supportTicket);
        return ResponseEntity.ok(createdTicket);
	}

	@Override
	public ResponseEntity<SupportTicket> getTicketById(@PathVariable Long id) {
		SupportTicket ticket = supportTicketService.getTicketById(id);
        return ResponseEntity.ok(ticket);
	}

	@Override
	public ResponseEntity<List<SupportTicket>> getAllTickets() {
		List<SupportTicket> tickets = supportTicketService.getAllTickets();
        return ResponseEntity.ok(tickets);
	}

	@Override
	public ResponseEntity<SupportTicket> updateTicket(Long id, SupportTicket supportTicket) {
		SupportTicket updatedTicket = supportTicketService.updateTicket(id, supportTicket);
        return ResponseEntity.ok(updatedTicket);
	}

	@Override
	public ResponseEntity<Void> deleteTicket(Long id) {
		supportTicketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
	}


}
