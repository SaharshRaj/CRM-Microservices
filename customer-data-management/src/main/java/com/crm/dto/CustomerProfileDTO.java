package com.crm.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for Customer Profile.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileDTO {

    /**
     * The unique ID of the customer.
     */
    private Long customerID;

    /**
     * The name of the customer.
     */
    @NotBlank
    private String name;

    /**
     * The email ID of the customer.
     */
    @NotBlank
    @Email(message = "Invalid email address")
    private String emailId;

    /**
     * The phone number of the customer.
     */
    @NotBlank
    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;

    /**
     * The purchase history of the customer.
     */
    private List<String> purchaseHistory;

    /**
     * The segmentation data of the customer.
     */
    private List<String> segmentationData;

}
