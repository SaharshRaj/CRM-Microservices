package com.crm.dto;

import com.crm.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignDTO {
    private Long campaignID;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Type type;
    private Integer customerInteractions;
}
