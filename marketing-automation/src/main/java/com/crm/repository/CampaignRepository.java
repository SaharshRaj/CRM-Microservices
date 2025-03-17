package com.crm.repository;

import com.crm.entities.Campaign;
import com.crm.enums.Type;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
	List<Campaign> findByType(Type type);
}