package com.crm.controller;

import com.crm.dto.ReportResponseDTO;
import com.crm.service.ReportService;
import com.crm.service.ReportServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportControllerImpl implements ReportController{

    @Autowired
    private ReportService service;

    /**
     * @return
     */
    @Override
    public ResponseEntity<ReportResponseDTO> generateCustomersReport() {
        try {
            ReportResponseDTO reportResponseDTO = service.generateCustomerReport();
            return ResponseEntity.ok(reportResponseDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return
     */
    @Override
    public ResponseEntity<ReportResponseDTO> generateSalesReport() {

        try {
           ReportResponseDTO reportResponseDTO = service.generateSalesReport();
        return ResponseEntity.ok(reportResponseDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return
     */
    @Override
    public ResponseEntity<ReportResponseDTO> generateSupportTicketsReport() {
        try {
            ReportResponseDTO reportResponseDTO = service.generateSupportReport();
            return ResponseEntity.ok(reportResponseDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return
     */
    @Override
    public ResponseEntity<ReportResponseDTO> generateMarketingReport() {
        try {
            ReportResponseDTO reportResponseDTO = service.generateMarketingReport();
            return ResponseEntity.ok(reportResponseDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param id
     * @return
     */
    @Override
    public ResponseEntity<ReportResponseDTO> getReportById(Long id) {
        return null;
    }
}
