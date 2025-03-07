package com.crm.repository;

import com.crm.entities.CustomerBehaviorReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerBehaviorReportRepository extends JpaRepository<CustomerBehaviorReport, Long> {
}