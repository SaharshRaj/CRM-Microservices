package com.crm.service;

import com.crm.dto.CustomerBehaviorReportDTO;
import com.crm.entities.CustomerBehaviorReport;
import com.crm.mapper.ReportMapper;
import com.crm.repository.CustomerBehaviorReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CustomerBehaviorReportServiceImpl implements CustomerBehaviorReportService {

    @Autowired
    private CustomerBehaviorReportRepository repository;


    /**
     * Retrieve all Customer Behavior Reports
     */
    @Override
    public List<CustomerBehaviorReportDTO> retrieveAllCustomerBehaviorReports() {
        // Retrieve all reports from repository
        log.info("--------METHOD CALLED--------");
        List<CustomerBehaviorReport> allCustomerBehaviorReports = repository.findAll();

        // Declare DTO list
        List<CustomerBehaviorReportDTO> resultList = new ArrayList<>();

        // Populate List
        allCustomerBehaviorReports.forEach(e -> {
            // Use MAPPER method to convert entity to DTO
            log.info("Entity: {}", e);
            CustomerBehaviorReportDTO customerBehaviorReportDTO = ReportMapper.MAPPER.mapToCustomerBehaviorReportDTO(e);
            // Add DTO to list
            log.info("DTO: {}", customerBehaviorReportDTO);
            resultList.add(customerBehaviorReportDTO);
        });

        // Return Result


        return resultList;
    }
}
