package com.crm.service;

import com.crm.dto.ReportResponseDTO;
import com.crm.dto.external.CampaignDTO;
import com.crm.dto.external.CustomerProfileDTO;
import com.crm.dto.external.SalesOpportunityResponseDTO;
import com.crm.dto.external.SupportTicketDTO;
import com.crm.dummy.CampaignMockService;
import com.crm.dummy.CustomerMockService;
import com.crm.dummy.SalesMockService;
import com.crm.dummy.SupportTicketMockService;
import com.crm.entities.Report;
import com.crm.enums.ReportType;
import com.crm.enums.SalesStage;
import com.crm.enums.Status;
import com.crm.enums.Type;
import com.crm.mapper.ReportMapper;
import com.crm.repository.ReportRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    private SalesMockService salesMockService;
    @Mock
    private CustomerMockService customerMockService;
    @Mock
    private SupportTicketMockService supportTicketMockService;
    @Mock
    private CampaignMockService campaignMockService;
    @Mock
    private ReportRepository repository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ReportMapper reportMapper;

    @InjectMocks
    private ReportServiceImpl reportService;

    private List<CustomerProfileDTO> customerProfiles;
    private List<SalesOpportunityResponseDTO> salesOpportunities;
    private List<SupportTicketDTO> supportTickets;
    private List<CampaignDTO> campaigns;

    @BeforeEach
    void setUp() {
        customerProfiles = Arrays.asList(
                CustomerProfileDTO.builder()
                        .customerID(1L)
                        .name("Customer1")
                        .emailId("email1")
                        .phoneNumber("phone1")
                        .purchaseHistory(Arrays.asList("product1"))
                        .build(),
                CustomerProfileDTO.builder()
                        .customerID(2L)
                        .name("Customer2")
                        .emailId("email2")
                        .phoneNumber("phone2")
                        .purchaseHistory(Arrays.asList("product2", "product3"))
                        .build()
        );
        salesOpportunities = Arrays.asList(
                SalesOpportunityResponseDTO.builder()
                        .opportunityID(1L)
                        .estimatedValue(BigDecimal.valueOf(1000))
                        .salesStage(SalesStage.CLOSED_WON)
                        .build(),
                SalesOpportunityResponseDTO.builder()
                        .opportunityID(2L)
                        .estimatedValue(BigDecimal.valueOf(2000))
                        .salesStage(SalesStage.PROSPECTING)
                        .build(),
                SalesOpportunityResponseDTO.builder()
                        .opportunityID(3L)
                        .estimatedValue(BigDecimal.valueOf(500))
                        .salesStage(SalesStage.CLOSED_LOST)
                        .build()
        );
        supportTickets = Arrays.asList(
                SupportTicketDTO.builder()
                        .ticketID(1L)
                        .customerID(1L)
                        .issueDescription("Issue1")
                        .status(Status.OPEN)
                        .assignedAgent(1L)
                        .build(),
                SupportTicketDTO.builder()
                        .ticketID(2L)
                        .customerID(2L)
                        .issueDescription("Issue2")
                        .status(Status.CLOSED)
                        .assignedAgent(2L)
                        .build()
        );
        campaigns = Arrays.asList(
                CampaignDTO.builder()
                        .campaignID(1L)
                        .name("Campaign1")
                        .type(Type.EMAIL)
                        .startDate(LocalDate.now().minusDays(10))
                        .endDate(LocalDate.now().plusDays(10))
                        .customerInteractions(100)
                        .build(),
                CampaignDTO.builder()
                        .campaignID(2L)
                        .name("Campaign2")
                        .type(Type.SOCIAL_MEDIA)
                        .startDate(LocalDate.now().minusDays(20))
                        .endDate(LocalDate.now().minusDays(5))
                        .customerInteractions(200)
                        .build(),
                CampaignDTO.builder()
                        .campaignID(3L)
                        .name("Campaign3")
                        .type(Type.EMAIL)
                        .startDate(LocalDate.now().plusDays(5))
                        .endDate(LocalDate.now().plusDays(20))
                        .customerInteractions(0)
                        .build()
        );
    }


    @Test
    void getReportById_Success() {
        String dataPointsJson = "{\"totalCustomers\":3, \"inactiveCustomers\":0, \"top5Customers\":[{\"customerId\":1, \"purchaseCount\":2, \"name\":\"John Doe\"}, {\"customerId\":2, \"purchaseCount\":2, \"name\":\"Jane Smith\"}, {\"customerId\":3, \"purchaseCount\":2, \"name\":\"Alice Johnson\"}]}";
        Report report = Report.builder().reportType(ReportType.CUSTOMER).id(1L).dataPoints(dataPointsJson).generatedDate(LocalDateTime.now()).build();
        when(repository.findById(1L)).thenReturn(Optional.of(report));
        when(reportMapper.MAPPER.mapToDto(report)).thenAnswer(
                invocation -> {
                    return ReportResponseDTO.builder()
                            .reportType(ReportType.CUSTOMER)
                            .id(1L)
                            .build();
                }
        );

        ReportResponseDTO response = reportService.getReportById(1L);

        assertNotNull(response);
    }

    @Test
    void getReportById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> reportService.getReportById(1L));
    }
}