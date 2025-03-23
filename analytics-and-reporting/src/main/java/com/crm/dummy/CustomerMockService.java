package com.crm.dummy;

import com.crm.dto.external.CustomerProfileDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CustomerMockService {
    List<CustomerProfileDTO> list = Arrays.asList(
            CustomerProfileDTO.builder()
                    .customerID(1L)
                    .name("John Doe")
                    .emailId("john.doe@example.com")
                    .phoneNumber("1234567890")
                    .purchaseHistory(Arrays.asList("ProductA", "ProductB"))
                    .segmentationData(Arrays.asList("Segment1", "Segment2"))
                    .build(),
            CustomerProfileDTO.builder()
                    .customerID(2L)
                    .name("Jane Smith")
                    .emailId("jane.smith@example.com")
                    .phoneNumber("9876543210")
                    .purchaseHistory(Arrays.asList("ProductC", "ProductD"))
                    .segmentationData(Arrays.asList("Segment3", "Segment4"))
                    .build(),
            CustomerProfileDTO.builder()
                    .customerID(3L)
                    .name("Alice Johnson")
                    .emailId("alice.johnson@example.com")
                    .phoneNumber("1122334455")
                    .purchaseHistory(Arrays.asList("ProductE", "ProductF"))
                    .segmentationData(Arrays.asList("Segment5", "Segment6"))
                    .build()
    );

    public ResponseEntity<List<CustomerProfileDTO>> getAllCustomers() {

        return ResponseEntity.ok(list);
    }
}
