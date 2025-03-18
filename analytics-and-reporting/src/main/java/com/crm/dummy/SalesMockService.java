package com.crm.dummy;

import com.crm.dto.external.SalesOpportunityResponseDTO;
import com.crm.enums.SalesStage;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class SalesMockService {
    List<SalesOpportunityResponseDTO> list = Arrays.asList(
            SalesOpportunityResponseDTO.builder().opportunityID(1L).customerID(1L).estimatedValue(new BigDecimal("10000.0")).salesStage(SalesStage.PROSPECTING).closingDate(LocalDate.now().plusDays(7)).followUpReminder(LocalDate.now().plusDays(7)).build(),
            SalesOpportunityResponseDTO.builder().opportunityID(2L).customerID(2L).estimatedValue(new BigDecimal("20000.0")).salesStage(SalesStage.QUALIFICATION).closingDate(LocalDate.now().plusDays(7)).followUpReminder(LocalDate.now().plusDays(7)).build(),
            SalesOpportunityResponseDTO.builder().opportunityID(3L).customerID(3L).estimatedValue(new BigDecimal("30000.0")).salesStage(SalesStage.CLOSED_LOST).closingDate(LocalDate.now().plusDays(7)).followUpReminder(LocalDate.now().plusDays(7)).build(),
            SalesOpportunityResponseDTO.builder().opportunityID(4L).customerID(4L).estimatedValue(new BigDecimal("40000.0")).salesStage(SalesStage.CLOSED_WON).closingDate(LocalDate.now().plusDays(7)).followUpReminder(LocalDate.now().plusDays(7)).build(),
            SalesOpportunityResponseDTO.builder().opportunityID(5L).customerID(5L).estimatedValue(new BigDecimal("50000.0")).salesStage(SalesStage.QUALIFICATION).closingDate(LocalDate.now().plusDays(7)).followUpReminder(LocalDate.now().plusDays(7)).build()
    );

    public List<SalesOpportunityResponseDTO> getAllLeads(){
        return list;
    }
}
