package com.crm.controller;

import com.crm.dto.SalesOpportunityDTO;
import com.crm.enums.SalesStage;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/sales-opportunity")
public interface SalesOpportunityController {

    @GetMapping
    ResponseEntity<List<SalesOpportunityDTO>> retrieveAllSalesOpportunities();

    @PostMapping
    ResponseEntity<SalesOpportunityDTO> createSalesOpportunity(@Valid  @RequestBody SalesOpportunityDTO salesOpportunityDto);

    @GetMapping("/{opportunityId}")
    ResponseEntity<SalesOpportunityDTO> getOpportunitiesByOpportunity(@PathVariable Long opportunityId);

    @GetMapping("/customer/{customerId}")
    ResponseEntity<List<SalesOpportunityDTO>> getOpportunitiesByCustomer(@PathVariable Long customerId);

    @GetMapping("/salesStage/{salesStage}")
    ResponseEntity<List<SalesOpportunityDTO>> getOpportunitiesBySalesStage(@PathVariable SalesStage salesStage);

    @GetMapping("/estimatedValue/{estimatedValue}")
    ResponseEntity<List<SalesOpportunityDTO>> getOpportunitiesByEstimatedValue(@PathVariable BigDecimal estimatedValue);

    @GetMapping("/closingDate/{closingDate}")
    ResponseEntity<List<SalesOpportunityDTO>> getOpportunitiesByClosingDate(@PathVariable LocalDate closingDate);

    @GetMapping("/followUpReminder/{followUpReminder}")
    ResponseEntity<List<SalesOpportunityDTO>> getOpportunitiesByFollowUpReminder(@PathVariable LocalDateTime followUpReminder);

    @PostMapping("/followUpReminder")
    ResponseEntity<SalesOpportunityDTO> scheduleFollowUpReminder(@RequestParam Long opportunityId, @RequestParam LocalDateTime reminderDate);

    @DeleteMapping("/{opportunityId}")
    ResponseEntity<String> deleteByOpportunityID(@PathVariable Long opportunityId);
}
