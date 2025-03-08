package com.crm.service;

import com.crm.dto.SalesOpportunityDTO;
import com.crm.entities.SalesOpportunity;
import com.crm.enums.SalesStage;
import com.crm.mapper.SalesOppurtunityMapper;
import com.crm.repository.SalesOpportunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalesOpportunityServiceImpl implements SalesOpportunityService {
    
    @Autowired
    private SalesOpportunityRepository repository;
    
    /**
     * Add Method Info for documentation
     */
    @Override
    public List<SalesOpportunityDTO> retrieveAllSalesOpportunities() {
        // Retreive all campaigns from repo
        List<SalesOpportunity> allSalesOpportunities = repository.findAll();

        // Declare DTO list
        List<SalesOpportunityDTO> resultList = new ArrayList<>();

        // Populate List
        allSalesOpportunities.forEach(e -> {
            // Use MAPPER method to convert entity to DTO
            SalesOpportunityDTO salesOpportunityDTO = SalesOppurtunityMapper.MAPPER.mapToDTO(e);
            // Add DTO to list
            resultList.add(salesOpportunityDTO);
        });
        //Return Result
        return resultList;
    }

    /**
     * @param salesOpportunityDto
     * @return
     */
    @Override
    public SalesOpportunityDTO createSalesOpportunity(SalesOpportunityDTO salesOpportunityDto) {
        return null;
    }

    /**
     * @param customerId
     * @return
     */
    @Override
    public List<SalesOpportunityDTO> getOpportunitiesByCustomer(Long customerId) {
        return List.of();
    }

    /**
     * @param salesStage
     * @return
     */
    @Override
    public List<SalesOpportunityDTO> getOpportunitiesBySalesStage(SalesStage salesStage) {
        return List.of();
    }

    /**
     * @param estimatedValue
     * @return
     */
    @Override
    public List<SalesOpportunityDTO> getOpportunitiesByEstimatedValue(BigDecimal estimatedValue) {
        return List.of();
    }

    /**
     * @param closingDate
     * @return
     */
    @Override
    public List<SalesOpportunityDTO> getOpportunitiesByClosingDate(LocalDate closingDate) {
        return List.of();
    }

    /**
     * @param followUpReminder
     * @return
     */
    @Override
    public List<SalesOpportunityDTO> getOpportunitiesByFollowUpReminder(LocalDateTime followUpReminder) {
        return List.of();
    }

    /**
     * @param opportunityId
     * @param reminderDate
     * @return
     */
    @Override
    public SalesOpportunityDTO scheduleFollowUpReminder(Long opportunityId, LocalDateTime reminderDate) {
        return null;
    }

    /**
     * @param opportunityId
     * @return
     */
    @Override
    public boolean deleteByOpportunityID(Long opportunityId) {
        return false;
    }
}
