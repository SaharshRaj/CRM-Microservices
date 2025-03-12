package com.crm.service;

//import com.crm.dto.CustomerProfileDTO;
import com.crm.dto.SupportTicketDTO;
import com.crm.entities.SupportTicket;

import java.util.List;


public interface SupportTicketService {
//	public List<SupportTicketDTO> retrieveAllProfiles();
    
    SupportTicket createTicket(SupportTicket supportTicket);
    SupportTicket updateTicket(Long ticketId, SupportTicket ticket);
    SupportTicket getTicketById(Long ticketId);
    List<SupportTicket> getAllTickets();
    void deleteTicket(Long ticketId);
	List<SupportTicketDTO> retrieveAllProfiles();

//    NotificationDTO sendNotification();
//    boolean  sendNotification();
}
