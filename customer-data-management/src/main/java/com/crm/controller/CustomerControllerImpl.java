package com.crm.controller;

import com.crm.dto.CustomerProfileDTO;
import com.crm.enums.PurchasingHabits;
import com.crm.exception.ResourceNotFoundException;
import com.crm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Override
    public ResponseEntity<?> getCustomerById(long customerId) throws ResourceNotFoundException {
        try {
            CustomerProfileDTO customerProfileDTO = service.getCustomerById(customerId);
            return ResponseEntity.ok(customerProfileDTO);
        } catch(ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer Profile Not Found");
        }
    }

    @Override
    public ResponseEntity<?> searchCustomerBasedOnEmailId(String emailId) throws ResourceNotFoundException {
        try{
            List<CustomerProfileDTO> customerProfileDTOList = service.searchCustomerBasedOnEmailId(emailId);
            return ResponseEntity.ok(customerProfileDTOList);
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Customer Profiles Found");
        }
    }

    @Override
    public ResponseEntity<?> searchCustomerBasedOnName(String name) throws ResourceNotFoundException {
        try{
            List<CustomerProfileDTO> customerProfileDTOList = service.searchCustomerBasedOnName(name);
            return ResponseEntity.ok(customerProfileDTOList);
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Customer Profiles Found");
        }
    }

    @Override
    public ResponseEntity<?> searchCustomerBasedOnPhoneNumber(String phoneNumber) throws ResourceNotFoundException {
        try{
            List<CustomerProfileDTO> customerProfileDTOList = service.searchCustomerBasedOnPhoneNumber(phoneNumber);
            return ResponseEntity.ok(customerProfileDTOList);
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Customer Profiles Found");
        }
    }

    @Override
    public ResponseEntity<?> displayCustomerPurchasingHabit(long customerId) throws ResourceNotFoundException {
        try{
            PurchasingHabits purchasingHabits = service.displayCustomerPurchasingHabit(customerId);
            return ResponseEntity.ok(purchasingHabits);
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Customer Profiles Found");
        }
    }

    @Override
    public ResponseEntity<?> addCustomerProfile(CustomerProfileDTO customerProfileDTO) throws ResourceNotFoundException {
        try{
            service.addCustomerProfile(customerProfileDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Customer Profile Added Sucessfully");
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer Profile Cannot Be Empty");
        }
    }

    @Override
    public ResponseEntity<?> updateCustomer(Long customerId, CustomerProfileDTO customerProfileDTO) throws ResourceNotFoundException {
        try {
            CustomerProfileDTO updatedCustomer = service.updateCustomer(customerId, customerProfileDTO);
            return ResponseEntity.ok(updatedCustomer);
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer Profile Updation Failed");
        }
    }

    @Override
    public ResponseEntity<?> updatePurchasingHabit(Long customerId) throws ResourceNotFoundException {
        try{
            CustomerProfileDTO customerDTO = service.updatePurchasingHabit(customerId);
            return ResponseEntity.ok(customerDTO);
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Updation Failed - Customerprofile Not Found");
        }
    }

    @Override
    public ResponseEntity<String> deleteCustomer(@PathVariable Long customerId) throws ResourceNotFoundException {
        try {
            service.deleteCustomer(customerId);
            return ResponseEntity.status(HttpStatus.OK).body("Customer Profile Deleted");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Delete failed - Customer Profile Not found");
        }
    }



}
