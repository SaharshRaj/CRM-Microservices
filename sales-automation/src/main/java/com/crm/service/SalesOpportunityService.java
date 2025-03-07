package com.crm.service;

import com.crm.dto.SalesOpportunityDTO;

import java.util.List;


public interface SalesOpportunityService {
    public List<SalesOpportunityDTO> retrieveAllSalesOpportunities();
}
