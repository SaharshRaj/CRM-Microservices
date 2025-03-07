package com.crm.controller;

import com.crm.dto.SupportTicketDTO;
import com.crm.service.SupportTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SupportTicketControllerImpl implements SupportTicketController {
    
    @Autowired
    private SupportTicketService service;
    
    /**
     * Add JavaDoc
     */
    @Override
    public ResponseEntity<List<SupportTicketDTO>> getAllSupportTickets() {
        List<SupportTicketDTO> customerProfileDTOS = service.retrieveAllProfiles();
        return new ResponseEntity<>(customerProfileDTOS, HttpStatus.OK);
    }
}
