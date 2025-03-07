package com.crm.dto;

import com.crm.entities.Agents;
import com.crm.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportTicketDTO {
    private Long ticketID;
    private Long customerID;
    private String issueDescription;
    private Long assignedAgent;
    private Status status;
}
