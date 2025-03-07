package com.crm.mapper;

import com.crm.dto.CustomerBehaviorReportDTO;
import com.crm.dto.CustomerSupportReportDTO;
import com.crm.dto.MarketingCampaignReportDTO;
import com.crm.dto.SalesPerformanceReportDTO;
import com.crm.entities.CustomerBehaviorReport;
import com.crm.entities.CustomerSupportReport;
import com.crm.entities.MarketingCampaignReport;
import com.crm.entities.SalesPerformanceReport;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReportMapper {

    //ReportMapper using Factory
    ReportMapper MAPPER = Mappers.getMapper(ReportMapper.class);


    //CustomerBehaviorReport to CustomerBehaviorReportDTO
    CustomerBehaviorReportDTO mapToCustomerBehaviorReportDTO(CustomerBehaviorReport customerBehaviorReport);
    //CustomerBehaviorReportDTO to CustomerBehaviorReport
    CustomerBehaviorReport mapToCustomerBehaviorReport(CustomerBehaviorReportDTO customerBehaviorReportDTO);

    //CustomerSupportReport to CustomerSupportReportDTO
    CustomerSupportReportDTO mapToCustomerSupportReportDTO(CustomerSupportReport customerSupportReport);
    //CustomerSupportReportDTO to CustomerSupportReport
    CustomerSupportReport mapToCustomerSupportReport(CustomerSupportReportDTO customerSupportReportDTO);

    //MarketingCampaignReport to MarketingCampaignReportDTO
    MarketingCampaignReportDTO mapToMarketingCampaignReportDTO(MarketingCampaignReport marketingCampaignReport);
    //MarketingCampaignReportDTO to MarketingCampaignReport
    MarketingCampaignReport mapToMarketingCampaignReport(MarketingCampaignReportDTO marketingCampaignReportDTO);

    //SalesPerformanceReport to SalesPerformanceReportDTO
    SalesPerformanceReportDTO mapToSalesPerformanceReportDTO(SalesPerformanceReport salesPerformanceReport);
    //SalesPerformanceReportDTO to SalesPerformanceReport
    SalesPerformanceReport mapToSalesPerformanceReport(SalesPerformanceReportDTO salesPerformanceReportDTO);

}
