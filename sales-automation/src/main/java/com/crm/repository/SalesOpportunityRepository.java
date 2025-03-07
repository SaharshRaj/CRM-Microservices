package com.crm.repository;

import com.crm.entities.SalesOpportunity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesOpportunityRepository extends JpaRepository<SalesOpportunity, Long> {
}