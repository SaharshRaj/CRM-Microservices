package com.crm.mapper;

import com.crm.dto.CampaignDTO;
import com.crm.entities.Campaign;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CampaignMapper {

    //CustomerProfileMapper using Factory
    CampaignMapper MAPPER = Mappers.getMapper(CampaignMapper.class);


    //CustomerProfile to CustomerProfileDTO
    CampaignDTO mapToDTO(Campaign campaign);
    //CustomerProfileDTO to CustomerProfile
    Campaign mapToCampaign(CampaignDTO supportTicketDTO);
}
