package com.crm.controller;

import com.crm.dto.*;
import com.crm.service.CustomerBehaviorReportService;
import com.crm.service.CustomerSupportReportService;
import com.crm.service.MarketingCampaignReportService;
import com.crm.service.SalesPerformanceReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class AnalyticsControllerImpl implements AnalyticsController {

    @Autowired
    private CustomerBehaviorReportService customerBehaviorReportService;

    @Autowired
    private CustomerSupportReportService customerSupportReportService;

    @Autowired
    private MarketingCampaignReportService marketingCampaignReportService;

    @Autowired
    private SalesPerformanceReportService salesPerformanceReportService;

    /**
     * Retrieve all reports.
     *
     * @return a ResponseEntity containing a list of ReportDTOs and an HTTP status
     */
    @Override
    public ResponseEntity<List<ReportDTO>> getAllReports() {
        List<CustomerBehaviorReportDTO> customerBehaviorReportDTOS = customerBehaviorReportService.retrieveAllCustomerBehaviorReports();
        List<CustomerSupportReportDTO> customerSupportReportDTOS = customerSupportReportService.retrieveAllCustomerSupportReports();
        List<MarketingCampaignReportDTO> marketingCampaignReportDTOS = marketingCampaignReportService.retrieveAllMarketingCampaignReports();
        List<SalesPerformanceReportDTO> salesPerformanceReportDTOS = salesPerformanceReportService.retrieveAllSalesPerformanceReports();

        List<ReportDTO> reportDTOList = new ArrayList<>(customerBehaviorReportDTOS);
        reportDTOList.addAll(customerSupportReportDTOS);
        reportDTOList.addAll(marketingCampaignReportDTOS);
        reportDTOList.addAll(salesPerformanceReportDTOS);

        return new ResponseEntity<>(reportDTOList, HttpStatus.OK);
    }
}
