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
public class CustomerBehaviorReportDTO implements ReportDTO{
    private Long reportID;
    private Long customerID;
    private List<String> dataPoints;
}
