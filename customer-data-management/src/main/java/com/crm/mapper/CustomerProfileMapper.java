package com.crm.mapper;

import com.crm.dto.CustomerProfileDTO;
import com.crm.entities.CustomerProfile;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link CustomerProfile} and its DTO {@link CustomerProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerProfileMapper {

    /**
     * Maps a {@link CustomerProfile} entity to a {@link CustomerProfileDTO}.
     *
     * @param customerProfile the customer profile entity to map
     * @return the mapped customer profile DTO
     */
    CustomerProfileDTO mapToDTO(CustomerProfile customerProfile);

    /**
     * Maps a {@link CustomerProfileDTO} to a {@link CustomerProfile} entity.
     *
     * @param customerProfileDTO the customer profile DTO to map
     * @return the mapped customer profile entity
     */
    CustomerProfile mapToCustomer(CustomerProfileDTO customerProfileDTO);
}