package com.crm.repository;

import com.crm.entities.CustomerSupportReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerSupportReportRepository extends JpaRepository<CustomerSupportReport, Long> {
}