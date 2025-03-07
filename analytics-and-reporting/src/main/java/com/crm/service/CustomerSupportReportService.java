package com.crm.service;

import com.crm.dto.CustomerSupportReportDTO;

import java.util.List;


public interface CustomerSupportReportService {
    public List<CustomerSupportReportDTO> retrieveAllCustomerSupportReports();
}
