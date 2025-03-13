package com.crm.controller;

import com.crm.dto.SalesOpportunityDTO;
import com.crm.dto.ScheduleConfigDTO;
import com.crm.enums.SalesStage;
import com.crm.exception.UnknownErrorOccurredException;
import com.crm.scheduler.DynamicSchedulerService;
import com.crm.service.SalesOpportunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
public class SalesOpportunityControllerImpl implements SalesOpportunityController {
    

    private final SalesOpportunityService service;
    private final DynamicSchedulerService schedulerService;

    @Autowired
    public SalesOpportunityControllerImpl(SalesOpportunityService service, DynamicSchedulerService schedulerService){
        this.service = service;
        this.schedulerService = schedulerService;
    }



    /**
     * Retrieves list of all available leads.
     *
     * @return a list of SalesOpportunityDTO objects representing all leads
     */
    @Override
    public ResponseEntity<List<SalesOpportunityDTO>> retrieveAllSalesOpportunities() {
        List<SalesOpportunityDTO> salesOpportunityDTOList = service.retrieveAllSalesOpportunities();
        return new ResponseEntity<>(salesOpportunityDTOList, HttpStatus.OK);
    }

    /**
     * Creates a new lead.
     *
     * @param salesOpportunityDto the DTO representing the lead to be created.
     * @return the created SalesOpportunityDTO object.
     */
    @Override
    public ResponseEntity<SalesOpportunityDTO> createSalesOpportunity(SalesOpportunityDTO salesOpportunityDto) {
        SalesOpportunityDTO salesOpportunity = service.createSalesOpportunity(salesOpportunityDto);
        return new ResponseEntity<>(salesOpportunity, HttpStatus.OK);
    }

    /**
     * Retrieves a lead by its opportunity ID.
     *
     * @param opportunityId the ID of the lead to be retrieved.
     * @return the SalesOpportunityDTO object representing the retrieved lead.
     */
    @Override
    public ResponseEntity<SalesOpportunityDTO> getOpportunitiesByOpportunity(Long opportunityId) {
        SalesOpportunityDTO result = service.getOpportunitiesByOpportunity(opportunityId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieves leads by customer ID
     *
     * @param customerId the ID of the customer whose leads are to be retrieved.
     * @return a list of SalesOpportunityDTO objects representing the retrieved leads.
     */
    @Override
    public ResponseEntity<List<SalesOpportunityDTO>> getOpportunitiesByCustomer(Long customerId) {
        List<SalesOpportunityDTO> result = service.getOpportunitiesByCustomer(customerId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieves leads by sales stage.
     *
     * @param salesStage the sales stage to filter leads by.
     * @returna list of SalesOpportunityDTO objects representing the retrieved leads.
     */
    @Override
    public ResponseEntity<List<SalesOpportunityDTO>> getOpportunitiesBySalesStage(SalesStage salesStage) {
        List<SalesOpportunityDTO> result = service.getOpportunitiesBySalesStage(salesStage);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieves leads by estimated value.
     *
     * @param estimatedValue the estimated value to filter leads by.
     * @return a list of SalesOpportunityDTO objects representing the retrieved leads.
     */
    @Override
    public ResponseEntity<List<SalesOpportunityDTO>> getOpportunitiesByEstimatedValue(BigDecimal estimatedValue) {
        List<SalesOpportunityDTO> result = service.getOpportunitiesByEstimatedValue(estimatedValue);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieves sales leads by closing date.
     *
     * @param closingDate the closing date to filter leads by.
     * @return a list of SalesOpportunityDTO objects representing the retrieved leads.
     */
    @Override
    public ResponseEntity<List<SalesOpportunityDTO>> getOpportunitiesByClosingDate(LocalDate closingDate) {
        List<SalesOpportunityDTO> result = service.getOpportunitiesByClosingDate(closingDate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieves leads by follow-up reminder date.
     *
     * @param followUpReminder the follow-up reminder date to filter leads by.
     * @returna list of SalesOpportunityDTO objects representing the retrieved leads.
     */
    @Override
    public ResponseEntity<List<SalesOpportunityDTO>> getOpportunitiesByFollowUpReminder(LocalDate followUpReminder) {
        List<SalesOpportunityDTO> result = service.getOpportunitiesByFollowUpReminder(followUpReminder);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Schedules a follow-up reminder for a lead.
     *
     * @param opportunityId the ID of the lead to schedule the reminder for.
     * @param reminderDate the date and time for the follow-up reminder.
     * @return the updated SalesOpportunityDTO object.
     */
    @Override
    public ResponseEntity<SalesOpportunityDTO> scheduleFollowUpReminder(Long opportunityId, LocalDate reminderDate) {
        SalesOpportunityDTO result = service.scheduleFollowUpReminder(opportunityId, reminderDate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Deletes a sales lead by its opportunity ID.
     *
     * @param opportunityId the ID of the lead to be deleted.
     * @return ResponseEntity with success message if the deletion was successful, ErrorResponseDTO otherwise.
     */
    @Override
    public ResponseEntity<String> deleteByOpportunityID(Long opportunityId) {
        boolean success = service.deleteByOpportunityID(opportunityId);
        if(success){
            return new ResponseEntity<>("{\"message\": \"Successfully deleted Lead with ID " + opportunityId + "\"}", HttpStatus.OK);
        }
        else{
            throw new UnknownErrorOccurredException("Some error occurred while deleting Lead with ID "+ opportunityId);
        }
    }

    /**
     * @param scheduleConfigDTO containing the new cron expression.
     * @return the updated scheduleConfigDTO object.
     */
    @Override
    public ResponseEntity<ScheduleConfigDTO> configCronJob(ScheduleConfigDTO scheduleConfigDTO) {
        ScheduleConfigDTO result = schedulerService.updateCronExpression(scheduleConfigDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
