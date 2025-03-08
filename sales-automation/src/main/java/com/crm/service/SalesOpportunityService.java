package com.crm.service;

import com.crm.dto.SalesOpportunityDTO;
import com.crm.enums.SalesStage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface SalesOpportunityService {
    List<SalesOpportunityDTO> retrieveAllSalesOpportunities();

    //Creates a new sales opportunity (deal) associated with a customer.
    SalesOpportunityDTO createSalesOpportunity(SalesOpportunityDTO salesOpportunityDto);

    //Retrieves all sales opportunities linked to a specific customer.
    List<SalesOpportunityDTO> getOpportunitiesByCustomer(Long customerId);


    //Retrieves all sales opportunities linked to a specific Sales Stage.
    List<SalesOpportunityDTO> getOpportunitiesBySalesStage(SalesStage salesStage);

    //Retrieves all sales opportunities linked to a specific Estimated Value.
    List<SalesOpportunityDTO> getOpportunitiesByEstimatedValue(BigDecimal estimatedValue);

    //Retrieves all sales opportunities linked to a specific Closing Date.
    List<SalesOpportunityDTO> getOpportunitiesByClosingDate(LocalDate closingDate);

    //Retrieves all sales opportunities linked to a specific follow-Up Reminder.
    List<SalesOpportunityDTO> getOpportunitiesByFollowUpReminder(LocalDateTime followUpReminder);

    //Creates a follow-up reminder
    SalesOpportunityDTO scheduleFollowUpReminder(Long opportunityId, LocalDateTime reminderDate);

    //Delete by id
    boolean deleteByOpportunityID(Long opportunityId);
}
