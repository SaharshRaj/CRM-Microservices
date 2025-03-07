package com.crm.controller;

import com.crm.dto.ReportDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("api/analytics")
public interface AnalyticsController {

    @GetMapping("")
    public ResponseEntity<List<ReportDTO>> getAllReports();

}
