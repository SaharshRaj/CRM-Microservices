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
public class CustomerSupportReportDTO implements ReportDTO{
    private Long reportID;
    private Long ticketID;
    private List<String> dataPoints;
}
