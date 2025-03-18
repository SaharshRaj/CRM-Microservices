package com.crm.controller;

import com.crm.dto.SupportTicketDTO;
import com.crm.enums.Status;
import com.crm.exception.UnknownErrorOccurredException;
import com.crm.service.SupportTicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SupportTicketControllerImpl implements SupportTicketController {

    private final SupportTicketService service;
    
    public SupportTicketControllerImpl(SupportTicketService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<List<SupportTicketDTO>> retrieveAllSupportTickets() {
        List<SupportTicketDTO> tickets = service.retrieveAllSupportTickets();
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SupportTicketDTO> createSupportTicket(SupportTicketDTO supportTicketDto) {
        SupportTicketDTO ticket = service.createSupportTicket(supportTicketDto);
        return new ResponseEntity<>(ticket, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<SupportTicketDTO> getSupportTicketById(Long ticketId) {
        SupportTicketDTO ticket = service.getSupportTicketById(ticketId);
        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<SupportTicketDTO>> getSupportTicketsByCustomer(Long customerId) {
        List<SupportTicketDTO> tickets = service.getSupportTicketsByCustomer(customerId);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<SupportTicketDTO>> getSupportTicketsByStatus(Status status) {
        List<SupportTicketDTO> tickets = service.getSupportTicketsByStatus(status);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SupportTicketDTO> updateTicketStatus(Long ticketId, Status status) {
        SupportTicketDTO ticket = service.updateTicketStatus(ticketId, status);
        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SupportTicketDTO> assignTicketToAgent(Long ticketId, Long agentId) {
        SupportTicketDTO ticket = service.assignTicketToAgent(ticketId, agentId);
        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteSupportTicketById(Long ticketId) {
        boolean success = service.deleteSupportTicketById(ticketId);
        if (success) {
            return new ResponseEntity<>("{\"message\": \"Successfully deleted Ticket with ID " + ticketId + "\"}", HttpStatus.OK);
        } else {
            throw new UnknownErrorOccurredException("Some error occurred while deleting Ticket with ID " + ticketId);
        }
    }

}
