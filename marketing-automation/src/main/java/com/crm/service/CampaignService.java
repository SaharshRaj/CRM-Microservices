package com.crm.service;

import com.crm.dto.CampaignDTO;

import java.util.List;


public interface CampaignService {
    public List<CampaignDTO> retrieveAllCampaigns();
}
