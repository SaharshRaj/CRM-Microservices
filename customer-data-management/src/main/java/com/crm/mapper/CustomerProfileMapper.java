package com.crm.mapper;

import com.crm.dto.CustomerProfileDTO;
import com.crm.entities.CustomerProfile;
import com.crm.entities.ContactInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerProfileMapper {

    CustomerProfileMapper MAPPER = Mappers.getMapper(CustomerProfileMapper.class);

    CustomerProfileDTO mapToDTO(CustomerProfile customerProfile);

    CustomerProfile mapToCustomer(CustomerProfileDTO customerProfileDTO);

    // Add custom mapping methods
    default String map(ContactInfo contactInfo) {
        // Implement logic to map ContactInfo to String
        return contactInfo.getEmail() + ";" + contactInfo.getPhone();
    }

    default ContactInfo map(String contactInfo) {
        // Implement logic to map String to ContactInfo
        String[] parts = contactInfo.split(";");
        return new ContactInfo(parts[0], parts[1]);
    }
}
