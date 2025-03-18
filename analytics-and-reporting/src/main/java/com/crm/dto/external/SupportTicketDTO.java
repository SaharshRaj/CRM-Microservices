package com.crm.dto.external;

import com.crm.enums.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupportTicketDTO {
    private Long ticketID;
    private Long customerID;
    private String issueDescription;
    private Long assignedAgent;
    private Status status;
}