package com.crm.dto;

import com.crm.enums.SalesStage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesOpportunityDTO {
    private Long opportunityID;
    private Long customerID;
    private SalesStage salesStage;
    private BigDecimal estimatedValue;
    private LocalDate closingDate;
    private LocalDateTime followUpReminder;
}
