package com.crm.controller;

import com.crm.dto.ReportResponseDTO;
import com.crm.entities.Report;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/analytics")
public interface ReportController {

    @PostMapping("/customers")
    public ResponseEntity<ReportResponseDTO> generateCustomersReport();

    @PostMapping("/sales")
    public ResponseEntity<ReportResponseDTO> generateSalesReport();

    @PostMapping("/supportTickets")
    public ResponseEntity<ReportResponseDTO> generateSupportTicketsReport();

    @PostMapping("/marketingCampaigns")
    public ResponseEntity<ReportResponseDTO> generateMarketingReport();

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponseDTO> getReportById(@PathVariable Long id);
}
