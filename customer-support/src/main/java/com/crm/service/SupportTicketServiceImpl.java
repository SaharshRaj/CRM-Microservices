package com.crm.service;

import com.crm.dto.SupportTicketDTO;
import com.crm.entities.SupportTicket;
import com.crm.mapper.SupportTicketMapper;
import com.crm.repository.SupportTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupportTicketServiceImpl implements SupportTicketService {
    
    @Autowired
    private SupportTicketRepository repository;
    
    /**
     * Add Method Info for documentation
     */
    @Override
    public List<SupportTicketDTO> retrieveAllProfiles() {
        // Retreive all profiles from repo
        List<SupportTicket> allProfiles = repository.findAll();

        // Declare DTO list
        List<SupportTicketDTO> resultList = new ArrayList<>();

        // Populate List
        allProfiles.forEach(e -> {
            // Use MAPPER method to convert entity to DTO
            SupportTicketDTO supportTicketDTO = SupportTicketMapper.MAPPER.mapToDTO(e);
            // Add DTO to list
            resultList.add(supportTicketDTO);
        });
        //Return Result
        return resultList;
    }
}
