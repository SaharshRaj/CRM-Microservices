package com.crm.service;

import com.crm.dto.SalesOpportunityDTO;
import com.crm.enums.SalesStage;
import com.crm.exception.InvalidDateTimeException;
import com.crm.exception.InvalidOpportunityIdException;
import com.crm.exception.InvalidSalesDetailsException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;


public interface SalesOpportunityService {
    List<SalesOpportunityDTO> retrieveAllSalesOpportunities() throws NoSuchElementException;

    //Creates a new sales opportunity (deal) associated with a customer.
    SalesOpportunityDTO createSalesOpportunity(SalesOpportunityDTO salesOpportunityDto) throws InvalidSalesDetailsException;

    SalesOpportunityDTO getOpportunitiesByOpportunity(Long opportunityId) throws NoSuchElementException;

    //Retrieves all sales opportunities linked to a specific customer.
    List<SalesOpportunityDTO> getOpportunitiesByCustomer(Long customerId) throws NoSuchElementException;


    //Retrieves all sales opportunities linked to a specific Sales Stage.
    List<SalesOpportunityDTO> getOpportunitiesBySalesStage(SalesStage salesStage) throws NoSuchElementException;

    //Retrieves all sales opportunities linked to a specific Estimated Value.
    List<SalesOpportunityDTO> getOpportunitiesByEstimatedValue(BigDecimal estimatedValue) throws NoSuchElementException;

    //Retrieves all sales opportunities linked to a specific Closing Date.
    List<SalesOpportunityDTO> getOpportunitiesByClosingDate(LocalDate closingDate) throws NoSuchElementException;

    //Retrieves all sales opportunities linked to a specific follow-Up Reminder.
    List<SalesOpportunityDTO> getOpportunitiesByFollowUpReminder(LocalDate followUpReminder) throws NoSuchElementException, InvalidOpportunityIdException;

    //Creates a follow-up reminder
    SalesOpportunityDTO scheduleFollowUpReminder(Long opportunityId, LocalDate reminderDate) throws InvalidDateTimeException, InvalidOpportunityIdException;

    //Delete by id
    boolean deleteByOpportunityID(Long opportunityId) throws InvalidOpportunityIdException;
}
