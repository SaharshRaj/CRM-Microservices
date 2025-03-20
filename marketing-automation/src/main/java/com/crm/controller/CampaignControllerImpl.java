package com.crm.controller;

import com.crm.dto.CampaignDTO;
import com.crm.enums.Type;
import com.crm.exception.CampaignNotFoundException;
import com.crm.service.CampaignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing marketing campaigns.
 * This controller provides endpoints for creating, retrieving, updating, and deleting campaigns,
 * as well as tracking campaign clicks.
 */
@RestController
@RequestMapping("api/marketing")
@Tag(name = "Campaign Management", description = "Operations related to marketing campaigns")
public class CampaignControllerImpl implements CampaignController {

    private final CampaignService service;

    /**
     * Constructs a new CampaignControllerImpl with the specified CampaignService.
     *
     * @param service The CampaignService to use for campaign operations.
     */
    public CampaignControllerImpl(CampaignService service) {
        this.service = service;
    }

    /**
     * Retrieves a list of all marketing campaigns.
     *
     * @return ResponseEntity containing a list of CampaignDTOs or NOT_FOUND if no campaigns are found.
     */
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

    /**
     * Creates a new marketing campaign.
     *
     * @param campaignDTO The CampaignDTO containing the details of the campaign to create.
     * @return ResponseEntity containing the created CampaignDTO or NOT_ACCEPTABLE if creation fails.
     */
    @Override
    @PostMapping("campaign")
    @Operation(summary = "Create a new campaign", description = "Creates a new marketing campaign.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Campaign created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CampaignDTO.class))),
            @ApiResponse(responseCode = "406", description = "Campaign creation failed")
    })
    public ResponseEntity<CampaignDTO> createCampaign(@Valid @RequestBody CampaignDTO campaignDTO) {
        try {
            CampaignDTO createdCampaign = service.createCampaign(campaignDTO);
            return new ResponseEntity<>(createdCampaign, HttpStatus.CREATED);
        } catch (CampaignNotFoundException e) {
            return new ResponseEntity<>(campaignDTO, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Retrieves a marketing campaign by its ID.
     *
     * @param campaignId The ID of the campaign to retrieve.
     * @return ResponseEntity containing the CampaignDTO or NOT_FOUND if the campaign is not found.
     */
    @Override
    @GetMapping("campaign/{campaignId}")
    @Operation(summary = "Get campaign by ID", description = "Retrieves a marketing campaign by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Campaign found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CampaignDTO.class))),
            @ApiResponse(responseCode = "404", description = "Campaign not found")
    })
    public ResponseEntity<CampaignDTO> getCampaignById(@PathVariable("campaignId") Long campaignId) {
        try {
            CampaignDTO campaign = service.getCampaignById(campaignId);
            return new ResponseEntity<>(campaign, HttpStatus.OK);
        } catch (CampaignNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates an existing marketing campaign by its ID.
     *
     * @param campaignId  The ID of the campaign to update.
     * @param campaignDTO The CampaignDTO containing the updated details.
     * @return ResponseEntity containing the updated CampaignDTO or NOT_MODIFIED if the update fails.
     */
    @Override
    @PutMapping("campaign/{campaignId}")
    @Operation(summary = "Update campaign by ID", description = "Updates an existing marketing campaign by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Campaign updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CampaignDTO.class))),
            @ApiResponse(responseCode = "304", description = "Campaign not modified")
    })
    public ResponseEntity<CampaignDTO> updateCampaign(@PathVariable("campaignId") Long campaignId,
                                                       @Valid @RequestBody CampaignDTO campaignDTO) {
        try {
            CampaignDTO campaign = service.updateCampaign(campaignId, campaignDTO);
            return new ResponseEntity<>(campaign, HttpStatus.OK);
        } catch (CampaignNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    /**
     * Deletes a marketing campaign by its ID.
     *
     * @param campaignId The ID of the campaign to delete.
     * @return ResponseEntity indicating the deletion status (ACCEPTED, BAD_REQUEST, or NOT_FOUND).
     */
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

    /**
     * Creates multiple marketing campaigns.
     *
     * @param campaignDTOs A list of CampaignDTOs containing the details of the campaigns to create.
     * @return ResponseEntity containing a list of created CampaignDTOs or BAD_REQUEST if creation fails.
     */
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

    /**
     * Tracks a click on a marketing campaign link and redirects to a success page.
     *
     * @param campaignId The ID of the campaign.
     * @return ResponseEntity indicating the redirect status (FOUND or NOT_FOUND).
     */
    @Override
    @GetMapping("/{campaignId}/track")
    @Operation(summary = "Track campaign click", description = "Tracks a click on a marketing campaign link and redirects to a success page.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Redirectto success page"),
            @ApiResponse(responseCode = "404", description = "Campaign not found")
    })
    public ResponseEntity<Void> trackCampaignClick(@PathVariable Long campaignId) {
        try {
            service.trackCampaignClick(campaignId);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("/success.html"))
                    .build();
        } catch (CampaignNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @Override
    public ResponseEntity<Map<Type, Map<String, Object>>> getReachAnalysisByType() {
        try {
            Map<Type, Map<String, Object>> analysisResults = service.getCampaignReachAnalysisByType();
            return ResponseEntity.ok(analysisResults);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}