package com.crm.repository;

import com.crm.entities.SalesPerformanceReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesPerformanceReportRepository extends JpaRepository<SalesPerformanceReport, Long> {
}