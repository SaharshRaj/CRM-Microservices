package com.crm.dummy;

import com.crm.dto.ErrorResponseDTO;
import com.crm.dto.external.CampaignDTO;
import com.crm.enums.Type;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class CampaignMockService {
    List<CampaignDTO> list = Arrays.asList(
            CampaignDTO.builder()
                    .campaignID(1L)
                    .name("Spring Sale Campaign")
                    .startDate(LocalDate.now().plusDays(1))
                    .endDate(LocalDate.now().plusDays(15))
                    .type(Type.EMAIL)
                    .customerInteractions(1200)
                    .trackingUrl("http://example.com/spring-sale")
                    .build(),
            CampaignDTO.builder()
                    .campaignID(2L)
                    .name("New Product Launch")
                    .startDate(LocalDate.now().plusDays(5))
                    .endDate(LocalDate.now().plusDays(20))
                    .type(Type.SMS)
                    .customerInteractions(800)
                    .trackingUrl("http://example.com/product-launch")
                    .build(),
            CampaignDTO.builder()
                    .campaignID(3L)
                    .name("End of Season Sale")
                    .startDate(LocalDate.now().plusDays(10))
                    .endDate(LocalDate.now().plusDays(25))
                    .type(Type.SMS)
                    .customerInteractions(2000)
                    .trackingUrl("http://example.com/eos-sale")
                    .build(),
            CampaignDTO.builder()
                    .campaignID(4L)
                    .name("Customer Feedback Drive")
                    .startDate(LocalDate.now().plusDays(2))
                    .endDate(LocalDate.now().plusDays(10))
                    .type(Type.EMAIL)
                    .customerInteractions(500)
                    .trackingUrl("http://example.com/feedback-drive")
                    .build(),
            CampaignDTO.builder()
                    .campaignID(5L)
                    .name("Referral Program Campaign")
                    .startDate(LocalDate.now().plusDays(3))
                    .endDate(LocalDate.now().plusDays(30))
                    .type(Type.SMS)
                    .customerInteractions(1500)
                    .trackingUrl("http://example.com/referral-program")
                    .build()
    );

    public ResponseEntity<List<CampaignDTO>> getAllCampaigns()
    {
        return ResponseEntity.ok(list);
    }
//    public ResponseEntity<ErrorResponseDTO> getAllCampaigns()
//    {
//        ErrorResponseDTO noCampaignAvailable = ErrorResponseDTO.builder().code("404").timestamp(LocalDateTime.now()).message("No Campaign Available").path("/api/blah").build();
//        return new ResponseEntity<>(noCampaignAvailable, HttpStatus.NOT_FOUND);
//    }
}
