package com.crm.controller;

import com.crm.dto.CampaignDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("api/marketing")
public interface CampaignController {

    @GetMapping("")
    public ResponseEntity<List<CampaignDTO>> getAllCampaigns();

}
