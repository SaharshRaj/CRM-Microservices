package com.crm.service;

import com.crm.dto.MarketingCampaignReportDTO;

import java.util.List;


public interface MarketingCampaignReportService {
    public List<MarketingCampaignReportDTO> retrieveAllMarketingCampaignReports();
}
