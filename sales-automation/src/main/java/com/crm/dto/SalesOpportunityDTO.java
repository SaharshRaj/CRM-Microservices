package com.crm.dto;

import com.crm.enums.SalesStage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class SalesOpportunityDTO {
    private Long opportunityID;

    @NotNull(message = "Customer ID can not be null")
    @Min(value = 1, message = "Customer ID must be greater than 0")
    private Long customerID;

    @NotNull(message = "Sales Stage cannot be null")
    private SalesStage salesStage;

    @NotNull(message = "Please enter estimated value.")
    @Min(value = 1, message = "Estimated Value must be greater than 0")
    private BigDecimal estimatedValue;

    @NotNull(message = "Please enter proper date.")
    private LocalDate closingDate;

    private LocalDate followUpReminder;
}
