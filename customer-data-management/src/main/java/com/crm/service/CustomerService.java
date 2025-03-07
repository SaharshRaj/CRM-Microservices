package com.crm.service;

import com.crm.dto.CustomerProfileDTO;

import java.util.List;


public interface CustomerService {
    public List<CustomerProfileDTO> retrieveAllProfiles();
}
