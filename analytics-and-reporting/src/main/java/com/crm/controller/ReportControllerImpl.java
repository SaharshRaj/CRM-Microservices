package com.crm.controller;
import com.crm.dto.ReportResponseDTO;
import com.crm.dto.ScheduleConfigRequestDTO;
import com.crm.dto.ScheduleConfigResponseDTO;
import com.crm.enums.ReportType;
import com.crm.exception.InvalidDataRecievedException;
import com.crm.repository.ReportRepository;
import com.crm.scheduler.DynamicSchedulerService;
import com.crm.service.ReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

import java.util.NoSuchElementException;


@RestController
@RequestMapping("/api/analytics")
public class ReportControllerImpl implements ReportController{

    @Autowired
    private ReportService service;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportRepository repository;

    @Autowired
    private DynamicSchedulerService schedulerService;

    /**
     * @return
     */
    @Override
    public ResponseEntity<ReportResponseDTO> generateCustomersReport() throws InvalidDataRecievedException, JsonProcessingException {
            ReportResponseDTO reportResponseDTO = service.generateCustomerReport();
            return new ResponseEntity<>(reportResponseDTO, HttpStatus.OK);
    }

    /**
     * @return
     */
    @Override
    public ResponseEntity<ReportResponseDTO> generateSalesReport() throws InvalidDataRecievedException, JsonProcessingException {


        ReportResponseDTO reportResponseDTO = service.generateSalesReport();
        return ResponseEntity.ok(reportResponseDTO);

    }

    /**
     * @return
     */
    @Override
    public ResponseEntity<ReportResponseDTO> generateSupportTicketsReport() throws InvalidDataRecievedException, JsonProcessingException {

            ReportResponseDTO reportResponseDTO = service.generateSupportReport();
            return ResponseEntity.ok(reportResponseDTO);

    }

    /**
     * @return
     */
    @Override
    public ResponseEntity<ReportResponseDTO> generateMarketingReport() throws InvalidDataRecievedException, JsonProcessingException {

            ReportResponseDTO reportResponseDTO = service.generateMarketingReport();
            return ResponseEntity.ok(reportResponseDTO);

    }
    /**
     * @param id
     * @return
     */
    @Override
    public ResponseEntity<ReportResponseDTO> getReportById(Long id) {
        ReportResponseDTO reportResponseDTO = service.getReportById(id);
        return ResponseEntity.ok(reportResponseDTO);
    }

    /**
     * @param scheduleConfigRequestDTO
     * @return
     */
    @Override
    public ResponseEntity<ScheduleConfigResponseDTO> configCronJob(ScheduleConfigRequestDTO scheduleConfigRequestDTO) {
        ScheduleConfigResponseDTO result = schedulerService.updateCronExpression(scheduleConfigRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ReportResponseDTO>> getReportByType(String type) {
        List<ReportResponseDTO> result = reportService.getReportByType(ReportType.valueOf(type));
        return ResponseEntity.ok(result);
    }
    @Override
    public ResponseEntity<List<ReportResponseDTO>> getAllReports() {
        List<ReportResponseDTO> reportResponseDTOs = reportService.getAllReports();
        return ResponseEntity.ok(reportResponseDTOs);
    }



}
