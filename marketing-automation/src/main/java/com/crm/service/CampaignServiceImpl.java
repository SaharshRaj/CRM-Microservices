package com.crm.service;

import com.crm.dto.CampaignDTO;
import com.crm.entities.Campaign;
import com.crm.mapper.CampaignMapper;
import com.crm.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CampaignServiceImpl implements CampaignService {
    
    @Autowired
    private CampaignRepository repository;
    
    /**
     * Add Method Info for documentation
     */
    @Override
    public List<CampaignDTO> retrieveAllCampaigns() {
        // Retreive all campaigns from repo
        List<Campaign> allCampaigns = repository.findAll();

        // Declare DTO list
        List<CampaignDTO> resultList = new ArrayList<>();

        // Populate List
        allCampaigns.forEach(e -> {
            // Use MAPPER method to convert entity to DTO
            CampaignDTO campaignDTO = CampaignMapper.MAPPER.mapToDTO(e);
            // Add DTO to list
            resultList.add(campaignDTO);
        });
        //Return Result
        return resultList;
    }
}
