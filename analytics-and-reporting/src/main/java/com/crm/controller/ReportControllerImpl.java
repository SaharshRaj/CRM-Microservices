package com.crm.controller;

import com.crm.dto.ReportResponseDTO;
import com.crm.dto.ScheduleConfigRequestDTO;
import com.crm.dto.ScheduleConfigResponseDTO;
import com.crm.exception.InvalidDataRecievedException;
import com.crm.scheduler.DynamicSchedulerService;
import com.crm.scheduler.SchedulerService;
import com.crm.service.ReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportControllerImpl implements ReportController{

    @Autowired
    private ReportService service;

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
}
