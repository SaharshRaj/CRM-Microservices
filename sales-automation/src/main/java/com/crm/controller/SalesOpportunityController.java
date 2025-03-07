package com.crm.controller;

import com.crm.dto.SalesOpportunityDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("api/sales")
public interface SalesOpportunityController {

    @GetMapping("")
    public ResponseEntity<List<SalesOpportunityDTO>> getAllSalesOpportunities();

}
