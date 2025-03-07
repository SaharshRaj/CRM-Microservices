package com.crm.service;

import com.crm.dto.SalesPerformanceReportDTO;
import com.crm.entities.SalesPerformanceReport;
import com.crm.mapper.ReportMapper;
import com.crm.repository.SalesPerformanceReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SalesPerformanceReportServiceImpl implements SalesPerformanceReportService {
    
    @Autowired
    private SalesPerformanceReportRepository repository;
    
    /**
     * Add Method Info for documentation
     */
    @Override
    public List<SalesPerformanceReportDTO> retrieveAllSalesPerformanceReports() {
        // Retreive all campaigns from repo
        List<SalesPerformanceReport> allSalesPerformanceReports = repository.findAll();

        // Declare DTO list
        List<SalesPerformanceReportDTO> resultList = new ArrayList<>();

        // Populate List
        allSalesPerformanceReports.forEach(e -> {
            // Use MAPPER method to convert entity to DTO
            SalesPerformanceReportDTO salesPerformanceReportDTO = ReportMapper.MAPPER.mapToSalesPerformanceReportDTO(e);
            // Add DTO to list
            resultList.add(salesPerformanceReportDTO);
        });
        //Return Result
        return resultList;
    }
}
