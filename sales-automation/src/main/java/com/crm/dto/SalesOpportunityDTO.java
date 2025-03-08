package com.crm.dto;

import com.crm.enums.SalesStage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class SalesOpportunityDTO {
    private Long opportunityID;
    @NotNull(message = "Lead should be related to customer.")
    @NotBlank(message = "Customer ID can not be blank")
    @Min(1L)
    private Long customerID;
    private SalesStage salesStage;
    @NotNull(message = "Please enter estimated value.")
    @NotBlank(message = "Estimated Value can not be blank")
    @Min(1)
    private BigDecimal estimatedValue;
    @NotNull(message = "Please enter proper date.")
    @NotBlank(message = "Date can not be blank")
    private LocalDate closingDate;
    private LocalDateTime followUpReminder;
}
