package com.crm.controller;

import com.crm.dto.CustomerProfileDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("api/customers")
public interface CustomerController {

    @GetMapping("")
    public ResponseEntity<List<CustomerProfileDTO>> getAllCustomerProfiles();

}
