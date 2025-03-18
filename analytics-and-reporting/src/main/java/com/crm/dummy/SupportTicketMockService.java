package com.crm.dummy;


import com.crm.dto.external.SupportTicketDTO;
import com.crm.enums.Status;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SupportTicketMockService {
    List<SupportTicketDTO> list = Arrays.asList(
            SupportTicketDTO.builder()
                    .ticketID(1L)
                    .customerID(101L)
                    .issueDescription("Unable to log in to the portal")
                    .assignedAgent(201L)
                    .status(Status.OPEN)
                    .build(),
            SupportTicketDTO.builder()
                    .ticketID(2L)
                    .customerID(102L)
                    .issueDescription("Payment not reflecting in account")
                    .assignedAgent(202L)
                    .status(Status.OPEN)
                    .build(),
            SupportTicketDTO.builder()
                    .ticketID(3L)
                    .customerID(103L)
                    .issueDescription("Error while updating profile")
                    .assignedAgent(203L)
                    .status(Status.CLOSED)
                    .build(),
            SupportTicketDTO.builder()
                    .ticketID(4L)
                    .customerID(104L)
                    .issueDescription("System crashes during checkout")
                    .assignedAgent(204L)
                    .status(Status.OPEN)
                    .build(),
            SupportTicketDTO.builder()
                    .ticketID(5L)
                    .customerID(105L)
                    .issueDescription("Password reset link not working")
                    .assignedAgent(205L)
                    .status(Status.CLOSED)
                    .build()
    );

    public List<SupportTicketDTO> getAllSupportTickets() {
        return list;
    }
}
