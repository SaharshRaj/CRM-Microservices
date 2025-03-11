package com.crm.mapper;

import com.crm.dto.CustomerProfileDTO;
import com.crm.entities.CustomerProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface CustomerProfileMapper {


    CustomerProfileDTO mapToDTO(CustomerProfile customerProfile);

    CustomerProfile mapToCustomer(CustomerProfileDTO customerProfileDTO);

    
}
