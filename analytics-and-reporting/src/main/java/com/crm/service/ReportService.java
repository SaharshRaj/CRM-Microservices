package com.crm.service;

import com.crm.dto.ReportResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ReportService {
    ReportResponseDTO generateCustomerReport() throws JsonProcessingException;
    ReportResponseDTO generateSalesReport() throws JsonProcessingException; //TODO: For opportunityLost and opportunityWon return estimatedValue instead of num
    ReportResponseDTO generateSupportReport() throws JsonProcessingException;
    ReportResponseDTO generateMarketingReport() throws JsonProcessingException;
}
