package com.crm.repository;

import com.crm.entities.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {
}