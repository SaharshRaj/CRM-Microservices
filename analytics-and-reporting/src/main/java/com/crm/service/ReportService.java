package com.crm.service;

import com.crm.dto.ReportResponseDTO;
import com.crm.exception.InvalidDataRecievedException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.NoSuchElementException;

public interface ReportService {
    ReportResponseDTO generateCustomerReport() throws JsonProcessingException, InvalidDataRecievedException;
    ReportResponseDTO generateSalesReport() throws JsonProcessingException, InvalidDataRecievedException;
    ReportResponseDTO generateSupportReport() throws JsonProcessingException, InvalidDataRecievedException;
    ReportResponseDTO generateMarketingReport() throws JsonProcessingException, InvalidDataRecievedException;
    ReportResponseDTO getReportById(Long reportId) throws NoSuchElementException;
}
