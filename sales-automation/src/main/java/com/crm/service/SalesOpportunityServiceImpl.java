package com.crm.service;

import com.crm.dto.SalesOpportunityDTO;
import com.crm.entities.SalesOpportunity;
import com.crm.mapper.SalesOppurtunityMapper;
import com.crm.repository.SalesOpportunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
