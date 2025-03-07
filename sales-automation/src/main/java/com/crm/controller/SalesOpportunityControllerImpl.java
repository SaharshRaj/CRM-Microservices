package com.crm.controller;

import com.crm.feign.CustomerDataManagement;
import com.crm.dto.CustomerProfileDTO;
import com.crm.dto.SalesOpportunityDTO;
import com.crm.service.SalesOpportunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SalesOpportunityControllerImpl implements SalesOpportunityController {
    
    @Autowired
    private SalesOpportunityService service;

    //autowire feignClient
    @Autowired
    private CustomerDataManagement customerDataManagement;
    
    /**
     * Add JavaDoc
     */
    @Override
    public ResponseEntity<List<SalesOpportunityDTO>> getAllSalesOpportunities() {
        List<SalesOpportunityDTO> salesOpportunityDTOS = service.retrieveAllSalesOpportunities();
        return new ResponseEntity<>(salesOpportunityDTOS, HttpStatus.OK);
    }


    //demo
    @GetMapping("customers")
    public ResponseEntity<List<CustomerProfileDTO>> getAllCustomers(){
            return customerDataManagement.getAllCustomerProfiles();
    }
}
