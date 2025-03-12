package com.crm.controller;

import com.crm.dto.CustomerProfileDTO;
import com.crm.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/customers")
public interface CustomerController {

    @GetMapping
    public ResponseEntity<List<CustomerProfileDTO>> getAllCustomerProfiles();

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomerById(@PathVariable long customerId) throws ResourceNotFoundException;

    @GetMapping("/email/{emailId}")
    public ResponseEntity<?> searchCustomerBasedOnEmailId(@PathVariable String emailId) throws ResourceNotFoundException;

    @GetMapping("/name/{name}")
    public ResponseEntity<?> searchCustomerBasedOnName(@PathVariable String name) throws ResourceNotFoundException;

    @GetMapping("/phoneNumber/{phoneNumber}")
    public ResponseEntity<?> searchCustomerBasedOnPhoneNumber(@PathVariable String phoneNumber) throws ResourceNotFoundException;

    @GetMapping("/purchasingHabit/{customerId}")
    public ResponseEntity<?> displayCustomerPurchasingHabit(@PathVariable long customerId) throws ResourceNotFoundException;

    @PostMapping()
    public ResponseEntity<?> addCustomerProfile(@RequestBody CustomerProfileDTO customerProfileDTO) throws ResourceNotFoundException;

    @PostMapping("/purchasingHabit/{customerId}")
    public ResponseEntity<?> updatePurchasingHabit(@PathVariable Long customerId) throws ResourceNotFoundException;

    @PostMapping("/{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long customerId, @RequestBody CustomerProfileDTO customerProfileDTO) throws ResourceNotFoundException;

    @DeleteMapping("/{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long customerId) throws ResourceNotFoundException;



}
