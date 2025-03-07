package com.crm.dto;

import com.crm.entities.SupportTicket;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentsDTO {
    private Long agentID;
    private String firstName;
    private String lastName;
}
