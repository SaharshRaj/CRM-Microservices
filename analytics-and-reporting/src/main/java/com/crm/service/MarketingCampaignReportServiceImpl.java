package com.crm.service;

import com.crm.dto.MarketingCampaignReportDTO;
import com.crm.entities.MarketingCampaignReport;
import com.crm.mapper.ReportMapper;
import com.crm.repository.MarketingCampaignReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarketingCampaignReportServiceImpl implements MarketingCampaignReportService {
    
    @Autowired
    private MarketingCampaignReportRepository repository;
    
    /**
     * Add Method Info for documentation
     */
    @Override
    public List<MarketingCampaignReportDTO> retrieveAllMarketingCampaignReports() {
        // Retreive all campaigns from repo
        List<MarketingCampaignReport> allMarketingCampaignReports = repository.findAll();

        // Declare DTO list
        List<MarketingCampaignReportDTO> resultList = new ArrayList<>();

        // Populate List
        allMarketingCampaignReports.forEach(e -> {
            // Use MAPPER method to convert entity to DTO
            MarketingCampaignReportDTO marketingCampaignReportDTO = ReportMapper.MAPPER.mapToMarketingCampaignReportDTO(e);
            // Add DTO to list
            resultList.add(marketingCampaignReportDTO);
        });
        //Return Result
        return resultList;
    }
}
