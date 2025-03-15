package com.crm.dto;

import jakarta.validation.constraints.*;
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

    @NotBlank
    private String name;

    @NotBlank
    @Email(message = "Invalid email address")
    private String emailId;

    @NotBlank
    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;

    private List<String> purchaseHistory;

    private List<String> segmentationData;

}
