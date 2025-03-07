package com.crm.controller;

import com.crm.dto.CampaignDTO;
import com.crm.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CampaignControllerImpl implements CampaignController {
    
    @Autowired
    private CampaignService service;
    
    /**
     * Add JavaDoc
     */
    @Override
    public ResponseEntity<List<CampaignDTO>> getAllCampaigns() {
        List<CampaignDTO> campaignDTOS = service.retrieveAllCampaigns();
        return new ResponseEntity<>(campaignDTOS, HttpStatus.OK);
    }
}
