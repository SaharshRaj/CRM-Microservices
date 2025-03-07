package com.crm.service;

import com.crm.dto.CustomerProfileDTO;
import com.crm.entities.CustomerProfile;
import com.crm.mapper.CustomerProfileMapper;
import com.crm.repository.CustomerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class customerServiceImpl implements CustomerService {
    
    @Autowired
    private CustomerProfileRepository repository;
    
    /**
     * Add Method Info for documentation
     */
    @Override
    public List<CustomerProfileDTO> retrieveAllProfiles() {
        // Retreive all profiles from repo
        List<CustomerProfile> allProfiles = repository.findAll();

        // Declare DTO list
        List<CustomerProfileDTO> resultList = new ArrayList<>();

        // Populate List
        allProfiles.forEach(e -> {
            // Use MAPPER method to convert entity to DTO
            CustomerProfileDTO customerProfileDTO = CustomerProfileMapper.MAPPER.mapToDTO(e);
            // Add DTO to list
            resultList.add(customerProfileDTO);
        });
        //Return Result
        return resultList;
    }
}
