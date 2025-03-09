package com.crm.controller;

import com.crm.dto.SalesOpportunityDTO;
import com.crm.enums.SalesStage;
import com.crm.service.SalesOpportunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class SalesOpportunityControllerImpl implements SalesOpportunityController {
    
    @Autowired
    private SalesOpportunityService service;



    /**
     * @return
     */
    @Override
    public ResponseEntity<List<SalesOpportunityDTO>> retrieveAllSalesOpportunities() {
        return null;
    }

    /**
     * @param salesOpportunityDto
     * @return
     */
    @Override
    public ResponseEntity<SalesOpportunityDTO> createSalesOpportunity(SalesOpportunityDTO salesOpportunityDto) {
        return null;
    }

    /**
     * @param opportunityId
     * @return
     */
    @Override
    public ResponseEntity<SalesOpportunityDTO> getOpportunitiesByOpportunity(Long opportunityId) {
        return null;
    }

    /**
     * @param customerId
     * @return
     */
    @Override
    public ResponseEntity<List<SalesOpportunityDTO>> getOpportunitiesByCustomer(Long customerId) {
        return null;
    }

    /**
     * @param salesStage
     * @return
     */
    @Override
    public ResponseEntity<List<SalesOpportunityDTO>> getOpportunitiesBySalesStage(SalesStage salesStage) {
        return null;
    }

    /**
     * @param estimatedValue
     * @return
     */
    @Override
    public ResponseEntity<List<SalesOpportunityDTO>> getOpportunitiesByEstimatedValue(BigDecimal estimatedValue) {
        return null;
    }

    /**
     * @param closingDate
     * @return
     */
    @Override
    public ResponseEntity<List<SalesOpportunityDTO>> getOpportunitiesByClosingDate(LocalDate closingDate) {
        return null;
    }

    /**
     * @param followUpReminder
     * @return
     */
    @Override
    public ResponseEntity<List<SalesOpportunityDTO>> getOpportunitiesByFollowUpReminder(LocalDateTime followUpReminder) {
        return null;
    }

    /**
     * @param opportunityId
     * @param reminderDate
     * @return
     */
    @Override
    public ResponseEntity<SalesOpportunityDTO> scheduleFollowUpReminder(Long opportunityId, LocalDateTime reminderDate) {
        return null;
    }

    /**
     * @param opportunityId
     * @return
     */
    @Override
    public ResponseEntity<String> deleteByOpportunityID(Long opportunityId) {
        return null;
    }
}
