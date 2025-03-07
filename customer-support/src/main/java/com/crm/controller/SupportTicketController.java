package com.crm.controller;

import com.crm.dto.SupportTicketDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("api/support")
public interface SupportTicketController {

    @GetMapping("")
    public ResponseEntity<List<SupportTicketDTO>> getAllSupportTickets();

}
