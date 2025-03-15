package com.crm.controller;

import com.crm.dto.CustomerProfileDTO;
import com.crm.enums.Interest;
import com.crm.enums.PurchasingHabits;
import com.crm.enums.Region;
import com.crm.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/customers")
public interface CustomerController {

    @GetMapping
    public ResponseEntity<?> getAllCustomerProfiles() throws ResourceNotFoundException;

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomerById(@PathVariable long customerId) throws ResourceNotFoundException;

    @GetMapping("/email/{emailId}")
    public ResponseEntity<?> searchCustomerBasedOnEmailId(@PathVariable @Email(message = "Invalid email address") String emailId) throws ResourceNotFoundException;

    @GetMapping("/name/{name}")
    public ResponseEntity<?> searchCustomerBasedOnName(@PathVariable @NotBlank String name) throws ResourceNotFoundException;

    @GetMapping("/phoneNumber/{phoneNumber}")
    public ResponseEntity<?> searchCustomerBasedOnPhoneNumber(@PathVariable  @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits") String phoneNumber) throws ResourceNotFoundException;

    @GetMapping("/region/{region}")
    public ResponseEntity<?> searchCustomerBasedOnRegion(@PathVariable Region region) throws ResourceNotFoundException;

    @GetMapping("/purchasingHabit/{purchasingHabits}")
    public ResponseEntity<?> searchCustomerBasedOnPurchasingHabit(@PathVariable PurchasingHabits purchasingHabits) throws ResourceNotFoundException;

    @GetMapping("/interest/{interest}")
    public ResponseEntity<?> searchCustomerBasedOnInterest(@PathVariable Interest interest) throws ResourceNotFoundException;

    @GetMapping("/region&interest/{region}/{interest}")
    public ResponseEntity<?> searchCustomerBasedOnRegionInterest(@PathVariable Region region,@PathVariable Interest interest) throws ResourceNotFoundException;

    @GetMapping("/interest&purchasingHabits/{interest}/{purchasingHabits}")
    public ResponseEntity<?> searchCustomerBasedOnInterestPurchasingHabits(@PathVariable Interest interest,@PathVariable PurchasingHabits purchasingHabits) throws ResourceNotFoundException;

    @GetMapping("/region&purchasingHabits/{region}/{purchasingHabits}")
    public ResponseEntity<?> searchCustomerBasedOnRegionPurchasingHabits(@PathVariable Region region,@PathVariable PurchasingHabits purchasingHabits) throws ResourceNotFoundException;

    @GetMapping("/demographics/{region}/{interest}/{purchasingHabits}")
    public ResponseEntity<?> searchCustomerBasedOnRegionInterestPurchasingHabits(@PathVariable Region region,@PathVariable Interest interest,@PathVariable PurchasingHabits purchasingHabits) throws ResourceNotFoundException;

    @PostMapping()
    public ResponseEntity<?> addCustomerProfile(@RequestBody @Valid CustomerProfileDTO customerProfileDTO) throws ResourceNotFoundException;

    @PostMapping("/purchasingHabit/{customerId}")
    public ResponseEntity<?> updatePurchasingHabit(@PathVariable long customerId) throws ResourceNotFoundException;

    @PostMapping("/{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable long customerId, @RequestBody @Valid CustomerProfileDTO customerProfileDTO) throws ResourceNotFoundException;

    @DeleteMapping("/{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable long customerId) throws ResourceNotFoundException;

}
