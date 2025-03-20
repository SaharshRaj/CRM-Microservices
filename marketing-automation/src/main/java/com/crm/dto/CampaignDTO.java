package com.crm.dto;
import com.crm.enums.Type;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import org.springframework.validation.annotation.Validated;

/**
 * Data Transfer Object (DTO) for representing campaign information.
 * This class encapsulates the data related to a campaign, including its ID, name, start and end dates, type,
 * customer interactions, and tracking URL. It also includes validation constraints to ensure data integrity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class CampaignDTO {
    /**
     * The unique identifier for the campaign.
     */
    private Long campaignID;
    /**
     * The name of the campaign.
     * Must not be blank and must be between 5 and 100 characters in length.
     */
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 5, max = 100, message = "Name must be between 5 and 100 characters")
    private String name;
    /**
     * The start date of the campaign.
     * Must not be null and must be a present or future date.
     */
    @NotNull(message = "Start date cannot be null")
    @FutureOrPresent(message = "Start date must be present or future")
    private LocalDate startDate;
    /**
     * The end date of the campaign.
     * Must not be null and must be a present or future date.
     */
    @NotNull(message = "End date cannot be null")
    @FutureOrPresent(message = "End date must be present or future")
    private LocalDate endDate;
    /**
     * The type of the campaign (e.g., EMAIL, SMS).
     * Must not be null.
     */
    @NotNull(message = "Type cannot be null")
    private Type type;
    /**
     * The number of customer interactions for the campaign.
     */
    private int customerInteractions;

    /**
     * The tracking URL associated with the campaign.
     */
    private String trackingUrl;

    /**
     * Checks if the end date is after or equal to the start date.
     * If either start or end date is null, returns true, as @NotNull validation will handle null checks.
     *
     * @return true if the end date is after or equal to the start date, false otherwise.
     */
    public boolean isEndDateAfterStartDate() {
        if (startDate == null || endDate == null) {
            return true; // If one of the dates is null, validation will fail with @NotNull.
        }
        return !endDate.isBefore(startDate);
    }
}