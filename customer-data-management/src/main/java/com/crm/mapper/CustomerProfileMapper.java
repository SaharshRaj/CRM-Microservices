package com.crm.mapper;

import com.crm.dto.CustomerProfileDTO;
import com.crm.entities.CustomerProfile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class CustomerProfileMapper {

    private final ObjectMapper objectMapper;

    public CustomerProfileMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public CustomerProfileDTO toDTO(CustomerProfile customerProfile) {
        CustomerProfileDTO dto = CustomerProfileDTO.builder()
                .customerID(customerProfile.getCustomerID())
                .name(customerProfile.getName())
                .emailId(customerProfile.getEmailId())
                .phoneNumber(customerProfile.getPhoneNumber())
                .purchaseHistory(customerProfile.getPurchaseHistory())
                .build();

        if (customerProfile.getSegmentationData() != null && !customerProfile.getSegmentationData().isEmpty()) {
            try {
                JsonNode jsonNode = objectMapper.readTree(customerProfile.getSegmentationData()).get("segmentationData");
                if (jsonNode != null) {
                    dto.setRegion(jsonNode.has("Region") ? com.crm.enums.Region.valueOf(jsonNode.get("Region").asText()) : null);
                    dto.setInterest(jsonNode.has("Interest") ? com.crm.enums.Interest.valueOf(jsonNode.get("Interest").asText()) : null);
                    dto.setPurchasingHabits(jsonNode.has("PurchasingHabits") ? com.crm.enums.PurchasingHabits.valueOf(jsonNode.get("PurchasingHabits").asText()) : null);
                }
            } catch (JsonProcessingException e) {
                log.error("JsonParsingException Occured -> {}", e.getMessage());
            }
        }

        return dto;
    }

    public CustomerProfile toEntity(CustomerProfileDTO customerProfileDTO) {
        CustomerProfile entity = CustomerProfile.builder()
                .customerID(customerProfileDTO.getCustomerID())
                .name(customerProfileDTO.getName())
                .emailId(customerProfileDTO.getEmailId())
                .phoneNumber(customerProfileDTO.getPhoneNumber())
                .purchaseHistory(customerProfileDTO.getPurchaseHistory())
                .build();

        if (customerProfileDTO.getRegion() != null || customerProfileDTO.getInterest() != null || customerProfileDTO.getPurchasingHabits() != null) {
            Map<String, String> segmentationDataMap = new HashMap<>();
            if (customerProfileDTO.getRegion() != null) {
                segmentationDataMap.put("Region", customerProfileDTO.getRegion().name());
            }
            if (customerProfileDTO.getInterest() != null) {
                segmentationDataMap.put("Interest", customerProfileDTO.getInterest().name());
            }
            if (customerProfileDTO.getPurchasingHabits() != null) {
                segmentationDataMap.put("PurchasingHabits", customerProfileDTO.getPurchasingHabits().name());
            }

            Map<String, Map<String, String>> wrapperMap = new HashMap<>();
            wrapperMap.put("segmentationData", segmentationDataMap);

            try {
                entity.setSegmentationData(objectMapper.writeValueAsString(wrapperMap));
            } catch (JsonProcessingException e) {
                // Handle JSON serialization exception (e.g., log error)
                log.error("JsonParsingException Occured -> {}", e.getMessage());
            }
        }

        return entity;
    }
}