package com.crm.controller;
 
import com.crm.dto.CampaignDTO;

import com.crm.dto.EmailDTO;

import com.crm.entities.Campaign;

import com.crm.exception.CampaignNotFoundException;

import com.crm.repository.CampaignRepository;

import com.crm.service.CampaignService;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.media.Content;

import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
 
import java.net.URI;

import java.util.HashMap;

import java.util.List;

import java.util.Map;

import java.util.Optional;
 
@RestController

@RequestMapping("api/marketing")

@Tag(name = "Campaign Management", description = "Operations related to marketing campaigns")

public class CampaignControllerImpl implements CampaignController {
 
    @Autowired

    private CampaignService service;
 
    @Autowired

    private CampaignRepository repository;
 
    private static final Logger logger = LoggerFactory.getLogger(CampaignControllerImpl.class);
 
    @Override

    @GetMapping("GetAllCampaigns")

    @Operation(summary = "Get all campaigns", description = "Retrieves a list of all marketing campaigns.")

    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Successfully retrieved list", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CampaignDTO.class))),

            @ApiResponse(responseCode = "404", description = "Campaigns not found")

    })

    public ResponseEntity<List<CampaignDTO>> getAllCampaigns() {

        try {

            List<CampaignDTO> campaignDTO = service.retrieveAllCampaigns();

            return new ResponseEntity<>(campaignDTO, HttpStatus.OK);

        } catch (CampaignNotFoundException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }

    }
 
    @Override

    @PostMapping("campaign")

    @Operation(summary = "Create a new campaign", description = "Creates a new marketing campaign.")

    @ApiResponses(value = {

            @ApiResponse(responseCode = "201", description = "Campaign created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CampaignDTO.class))),

            @ApiResponse(responseCode = "406", description = "Campaign creation failed")

    })

    public ResponseEntity<?> createCampaign(@Valid @RequestBody CampaignDTO campaignDTO) {

        try {

            CampaignDTO createdCampaign = service.createCampaign(campaignDTO);

            return new ResponseEntity<>(createdCampaign, HttpStatus.CREATED);

        } catch (CampaignNotFoundException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);

        }

    }
 
    private void mockSendCampaignEmail(CampaignDTO campaignDTO) {

        // Simulate sending an email without actually sending it

        logger.info("Sending email for campaign: {}", campaignDTO.getName());

        logger.info("Campaign details: {}", campaignDTO);

        // You can add more detailed logging or other mock behavior here

    }
 
    @Override

    @GetMapping("campaign/{campaignId}")

    @Operation(summary = "Get campaign by ID", description = "Retrieves a marketing campaign by its ID.")

    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Campaign found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CampaignDTO.class))),

            @ApiResponse(responseCode = "404", description = "Campaign not found")

    })

    public ResponseEntity<?> getCampaignById(@PathVariable("campaignId") Long campaignId) {

        try {

            CampaignDTO campaign = service.getCampaignById(campaignId);

            return new ResponseEntity<>(campaign, HttpStatus.OK);

        } catch (CampaignNotFoundException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        }

    }
 
    @Override

    @PutMapping("campaign/{campaignId}")

    @Operation(summary = "Update campaign by ID", description = "Updates an existing marketing campaign by its ID.")

    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Campaign updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CampaignDTO.class))),

            @ApiResponse(responseCode = "304", description = "Campaign not modified")

    })

    public ResponseEntity<CampaignDTO> updateCampaign(@PathVariable("campaignId") Long campaignId, @Valid @RequestBody CampaignDTO campaignDTO) {

        try {

            CampaignDTO campaign = service.updateCampaign(campaignId, campaignDTO);

            return new ResponseEntity<>(campaign, HttpStatus.OK);

        } catch (CampaignNotFoundException e) {

            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        }

    }
 
    @Override

    @DeleteMapping("campaign/{campaignId}")

    @Operation(summary = "Delete campaign by ID", description = "Deletes a marketing campaign by its ID.")

    @ApiResponses(value = {

            @ApiResponse(responseCode = "202", description = "Campaign deleted"),

            @ApiResponse(responseCode = "400", description = "Bad request"),

            @ApiResponse(responseCode = "404", description = "Campaign not found")

    })

    public ResponseEntity<CampaignDTO> deleteCampaign(@PathVariable("campaignId") Long campaignId) {

        try {

            boolean isDeleted = service.deleteCampaign(campaignId);

            if (isDeleted) {

                return new ResponseEntity<>(HttpStatus.ACCEPTED);

            } else {

                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            }

        } catch (CampaignNotFoundException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }

    }
 
    @Override

    @PostMapping("multipleCampaigns")

    @Operation(summary = "Create multiple campaigns", description = "Creates multiple marketing campaigns.")

    @ApiResponses(value = {

            @ApiResponse(responseCode = "201", description = "Campaigns created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CampaignDTO.class))),

            @ApiResponse(responseCode = "400", description = "Bad request")

    })

    public ResponseEntity<List<CampaignDTO>> createCampaigns(@RequestBody List<CampaignDTO> campaignDTOs) {

        try {

            List<CampaignDTO> createdCampaigns = service.createCampaigns(campaignDTOs);

            return new ResponseEntity<>(createdCampaigns, HttpStatus.CREATED);

        } catch (CampaignNotFoundException | NullPointerException e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

    }
 
    @Override

    @GetMapping("/{campaignId}/track")

    @Operation(summary = "Track campaign click", description = "Tracks a click on a campaign link.")

    @ApiResponses(value = {

            @ApiResponse(responseCode = "302", description = "Redirected to success page"),

            @ApiResponse(responseCode = "404", description = "Campaign not found")

    })

    public ResponseEntity<String> trackCampaignClick(@PathVariable Long campaignId) {

        Optional<Campaign> campaignOpt = repository.findById(campaignId);
 
        if (campaignOpt.isPresent()) {

            Campaign campaign = campaignOpt.get();

            campaign.setCustomerInteractions(campaign.getCustomerInteractions() + 1);

            repository.save(campaign);

            String redirectUrl = "http://localhost:3004/success.html";

            logger.info("Campaign ID: {}, Customer Interactions: {}, Redirecting to: {}", campaignId, campaign.getCustomerInteractions(), redirectUrl);

            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectUrl)).build();

        } else {

            logger.warn("Campaign Not Found with ID: {}", campaignId);

            return ResponseEntity.notFound().build();

        }

    }

}
 