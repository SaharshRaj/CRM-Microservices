package com.crm.controller;

import com.crm.dto.CustomerProfileDTO;
import com.crm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerControllerImpl implements CustomerController{
    
    @Autowired
    private CustomerService service;
    
    /**
     * Add JavaDoc
     */
    @Override
    public ResponseEntity<List<CustomerProfileDTO>> getAllCustomerProfiles() {
        List<CustomerProfileDTO> customerProfileDTOS = service.retrieveAllProfiles();
        return new ResponseEntity<>(customerProfileDTOS, HttpStatus.OK);
    }
}
