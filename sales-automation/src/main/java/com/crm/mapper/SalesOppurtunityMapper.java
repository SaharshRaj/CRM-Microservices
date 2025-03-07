package com.crm.mapper;

import com.crm.dto.SalesOpportunityDTO;
import com.crm.entities.SalesOpportunity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SalesOppurtunityMapper {

    //SalesOppurtunityMapper using Factory
    SalesOppurtunityMapper MAPPER = Mappers.getMapper(SalesOppurtunityMapper.class);


    //SalesOpportunity to SalesOpportunityDTO
    SalesOpportunityDTO mapToDTO(SalesOpportunity salesOpportunity);
    //SalesOpportunityDTO to SalesOpportunity
    SalesOpportunity mapToNotification(SalesOpportunityDTO salesOpportunityDTO);
}
