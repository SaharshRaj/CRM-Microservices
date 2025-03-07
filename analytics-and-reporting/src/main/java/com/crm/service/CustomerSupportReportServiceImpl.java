package com.crm.service;

import com.crm.dto.CustomerSupportReportDTO;
import com.crm.entities.CustomerSupportReport;
import com.crm.mapper.ReportMapper;
import com.crm.repository.CustomerSupportReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerSupportReportServiceImpl implements CustomerSupportReportService {
    
    @Autowired
    private CustomerSupportReportRepository repository;
    
    /**
     * Add Method Info for documentation
     */
    @Override
    public List<CustomerSupportReportDTO> retrieveAllCustomerSupportReports() {
        // Retreive all campaigns from repo
        List<CustomerSupportReport> allCustomerSupportReports = repository.findAll();

        // Declare DTO list
        List<CustomerSupportReportDTO> resultList = new ArrayList<>();

        // Populate List
        allCustomerSupportReports.forEach(e -> {
            // Use MAPPER method to convert entity to DTO
            CustomerSupportReportDTO customerSupportReportDTO = ReportMapper.MAPPER.mapToCustomerSupportReportDTO(e);
            // Add DTO to list
            resultList.add(customerSupportReportDTO);
        });
        //Return Result
        return resultList;
    }
}
