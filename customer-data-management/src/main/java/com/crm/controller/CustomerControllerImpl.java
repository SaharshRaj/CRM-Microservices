package com.crm.controller;

import com.crm.dto.CustomerProfileDTO;
import com.crm.enums.Interest;
import com.crm.enums.PurchasingHabits;
import com.crm.enums.Region;
import com.crm.exception.ResourceNotFoundException;
import com.crm.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller implementation for managing customer profiles.
 */
@RestController
@Validated
public class CustomerControllerImpl implements CustomerController{


    private final CustomerService service;

    @Autowired
    public CustomerControllerImpl(CustomerService service){
        this.service=service;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation (summary = "Get All Customer Profiles", description = "Retrieves a list of all customer profiles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "404", description = "No existing customer profiles")
    })

    public ResponseEntity<?> getAllCustomerProfiles() throws ResourceNotFoundException{
                List<CustomerProfileDTO> customerProfileDTOList = service.retrieveAllProfiles();
                return ResponseEntity.ok(customerProfileDTOList);
        }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Get Customer by ID", description = "Retrieves customer profile by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved customer profile"),
            @ApiResponse(responseCode = "404", description = "Customer profile not found")
    })

    public ResponseEntity<?> getCustomerById(long customerId) throws ResourceNotFoundException {
            CustomerProfileDTO customerProfileDTO = service.getCustomerById(customerId);
            return ResponseEntity.ok(customerProfileDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Search Customer by Email ID", description = "Searches customer profiles based on email ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved customer profiles"),
            @ApiResponse(responseCode = "404", description = "No customer profiles found")
    })
    public ResponseEntity<?> searchCustomerBasedOnEmailId(String emailId) throws ResourceNotFoundException {
            List<CustomerProfileDTO> customerProfileDTOList = service.searchCustomerBasedOnEmailId(emailId);
            return ResponseEntity.ok(customerProfileDTOList);
        }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Search Customer by Name", description = "Searches customer profiles based on name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved customer profiles"),
            @ApiResponse(responseCode = "404", description = "No customer profiles found")
    })

    public ResponseEntity<?> searchCustomerBasedOnName(String name) throws ResourceNotFoundException {
        List<CustomerProfileDTO> customerProfileDTOList = service.searchCustomerBasedOnName(name);
        return ResponseEntity.ok(customerProfileDTOList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Search Customer by Phone Number", description = "Searches customer profiles based on phone number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved customer profiles"),
            @ApiResponse(responseCode = "404", description = "No customer profiles found")
    })

    public ResponseEntity<?> searchCustomerBasedOnPhoneNumber(String phoneNumber) throws ResourceNotFoundException {
        List<CustomerProfileDTO> customerProfileDTOList = service.searchCustomerBasedOnPhoneNumber(phoneNumber);
        return ResponseEntity.ok(customerProfileDTOList);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Search Customer by Region", description = "Searches customer profiles based on region")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved customer profiles"),
            @ApiResponse(responseCode = "404", description = "No customer profiles found")
    })
    public ResponseEntity<?> searchCustomerBasedOnRegion(Region region) throws ResourceNotFoundException {
        List<CustomerProfileDTO> customerProfileDTOList = service.searchCustomerBasedOnRegion(region);
        return ResponseEntity.ok(customerProfileDTOList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Search Customer by Purchasing Habit", description = "Searches customer profiles based on purchasing habits")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved customer profiles"),
            @ApiResponse(responseCode = "404", description = "No customer profiles found")
    })
    public ResponseEntity<?> searchCustomerBasedOnPurchasingHabit(PurchasingHabits purchasingHabits) throws ResourceNotFoundException {
        List<CustomerProfileDTO> customerProfileDTOList = service.searchCustomerBasedOnPurchasingHabit(purchasingHabits);
        return ResponseEntity.ok(customerProfileDTOList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Search Customer by Interest", description = "Searches customer profiles based on interest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved customer profiles"),
            @ApiResponse(responseCode = "404", description = "No customer profiles found")
    })
    public ResponseEntity<?> searchCustomerBasedOnInterest(Interest interest) throws ResourceNotFoundException {
        List<CustomerProfileDTO> customerProfileDTOList = service.searchCustomerBasedOnInterest(interest);
        return ResponseEntity.ok(customerProfileDTOList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Search Customer by Region and Interest", description = "Searches customer profiles based on region and interest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved customer profiles"),
            @ApiResponse(responseCode = "404", description = "No customer profiles found")
    })
    public ResponseEntity<?> searchCustomerBasedOnRegionInterest(Region region, Interest interest) throws ResourceNotFoundException {
        List<CustomerProfileDTO> customerProfileDTOList = service.searchCustomerBasedOnRegionAndInterest(region,interest);
        return ResponseEntity.ok(customerProfileDTOList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Search Customer by Interest and Purchasing Habits", description = "Searches customer profiles based on interest and purchasing habits")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved customer profiles"),
            @ApiResponse(responseCode = "404", description = "No customer profiles found")
    })
    public ResponseEntity<?> searchCustomerBasedOnInterestPurchasingHabits( Interest interest, PurchasingHabits purchasingHabits) throws ResourceNotFoundException {
        List<CustomerProfileDTO> customerProfileDTOList = service.searchCustomerBasedOnInterestAndPurchasingHabit(interest, purchasingHabits);
        return ResponseEntity.ok(customerProfileDTOList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Search Customer by Region and Purchasing Habits", description = "Searches customer profiles based on region and purchasing habits")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved customer profiles"),
            @ApiResponse(responseCode = "404", description = "No customer profiles found")
    })
    public ResponseEntity<?> searchCustomerBasedOnRegionPurchasingHabits(Region region, PurchasingHabits purchasingHabits) throws ResourceNotFoundException {
        List<CustomerProfileDTO> customerProfileDTOList = service.searchCustomerBasedOnRegionAndPurchasingHabit(region, purchasingHabits);
        return ResponseEntity.ok(customerProfileDTOList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Search Customer by Region, Interest, and Purchasing Habits", description = "Searches customer profiles based on region, interest, and purchasing habits")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved customer profiles"),
            @ApiResponse(responseCode = "404", description = "No customer profiles found")
    })
    public ResponseEntity<?> searchCustomerBasedOnRegionInterestPurchasingHabits(Region region, Interest interest, PurchasingHabits purchasingHabits) throws ResourceNotFoundException {
        List<CustomerProfileDTO> customerProfileDTOList = service.searchCustomerBasedOnRegionAndInterestAndPurchasingHabit(region, interest,purchasingHabits);
        return ResponseEntity.ok(customerProfileDTOList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Add Customer Profile", description = "Adds a new customer profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer profile added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid customer profile data")
    })
    public ResponseEntity<?> addCustomerProfile(CustomerProfileDTO customerProfileDTO) throws ResourceNotFoundException {
        service.addCustomerProfile(customerProfileDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Customer Profile Added Successfully");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Update Customer Profile", description = "Updates an existing customer profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer profile updated successfully"),
            @ApiResponse(responseCode = "404", description = "Customer profile not found")
    })
    public ResponseEntity<?> updateCustomer(long customerId, CustomerProfileDTO customerProfileDTO) throws ResourceNotFoundException {
        CustomerProfileDTO updatedCustomer = service.updateCustomer(customerId, customerProfileDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Add a single purchase to the customer's purchase history", description = "Adds a single purchase to the existing purchase history of a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer profile updated successfully"),
            @ApiResponse(responseCode = "404", description = "Customer profile not found")
    })
    public ResponseEntity<String> addPurchaseToPurchaseHistory(long customerId, String purchase) throws ResourceNotFoundException {
        service.addToPurchaseHistory(customerId, purchase);
        return ResponseEntity.ok("Purchase History updated successfully");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Add multiple purchases to the customer's purchase history", description = "Adds multiple purchases to the existing purchase history of a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer profile updated successfully"),
            @ApiResponse(responseCode = "404", description = "Customer profile not found")
    })
    public ResponseEntity<String> addMultiplePurchaseToPurchaseHistory(long customerId, List<String> purchase) throws ResourceNotFoundException {
        service.addMultiplePurchasesToPurchaseHistory(customerId, purchase);
        return ResponseEntity.ok("Purchase History updated successfully");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Update Purchasing Habit", description = "Updates the purchasing habit of a customer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated purchasing habit"),
            @ApiResponse(responseCode = "404", description = "Customer profile not found")
    })
    public ResponseEntity<?> updatePurchasingHabit(long customerId) throws ResourceNotFoundException {
        CustomerProfileDTO customerDTO = service.updatePurchasingHabit(customerId);
        return ResponseEntity.ok(customerDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Delete Customer Profile", description = "Deletes a customer profile by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer profile deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer profile not found")
    })
    public ResponseEntity<String> deleteCustomer( long customerId) throws ResourceNotFoundException {
        service.deleteCustomer(customerId);
        return ResponseEntity.status(HttpStatus.OK).body("Customer Profile Deleted");
    }

}
