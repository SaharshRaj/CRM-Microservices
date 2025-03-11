package com.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileDTO {
    private Long customerID;
    private String name;
    private String emailId;
    private String phoneNumber;
    private List<String> purchaseHistory;
    private List<String> segmentationData;
}
