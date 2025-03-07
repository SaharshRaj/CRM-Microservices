package com.crm.service;

import com.crm.dto.SalesPerformanceReportDTO;

import java.util.List;


public interface SalesPerformanceReportService {
    public List<SalesPerformanceReportDTO> retrieveAllSalesPerformanceReports();
}
