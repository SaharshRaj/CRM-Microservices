package com.crm.service;

import com.crm.dto.CustomerProfileDTO;
import com.crm.dto.SupportTicketDTO;
import com.crm.entities.SupportTicket;

import java.util.List;


public interface SupportTicketService {
    public List<SupportTicketDTO> retrieveAllProfiles();
}
