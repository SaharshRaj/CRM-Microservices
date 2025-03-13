package com.crm.mapper;

import com.crm.dto.SalesOpportunityDTO;
import com.crm.dto.ScheduleConfigDTO;
import com.crm.entities.SalesOpportunity;
import com.crm.entities.ScheduleConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SalesOpportunityMapper {

    //SalesOpportunityMapper using Factory
    SalesOpportunityMapper MAPPER = Mappers.getMapper(SalesOpportunityMapper.class);


    //SalesOpportunity to SalesOpportunityDTO
    SalesOpportunityDTO mapToDTO(SalesOpportunity salesOpportunity);
    //SalesOpportunityDTO to SalesOpportunity
    SalesOpportunity mapToSalesOpportunity(SalesOpportunityDTO salesOpportunityDTO);
    //SalesOpportunityDTO to SalesOpportunity
    ScheduleConfigDTO mapToScheduleConfigDTO(ScheduleConfig scheduleConfig);
    ScheduleConfig mapToScheduleConfig(ScheduleConfigDTO scheduleConfig);
}
