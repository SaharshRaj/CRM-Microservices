package com.crm.mapper;

import org.mapstruct.Mapper;

import com.crm.dto.CampaignDTO;
import com.crm.entities.Campaign;

@Mapper(componentModel = "spring") // Add this annotation
public interface CampaignMapper {

    CampaignDTO mapToDTO(Campaign campaign);

    Campaign mapToCampaign(CampaignDTO campaignDTO);
}