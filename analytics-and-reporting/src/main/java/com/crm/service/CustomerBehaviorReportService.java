package com.crm.service;

import com.crm.dto.CustomerBehaviorReportDTO;

import java.util.List;


public interface CustomerBehaviorReportService {
    public List<CustomerBehaviorReportDTO> retrieveAllCustomerBehaviorReports();
}
