package com.crm.controller;

import com.crm.dto.ErrorResponseDTO;
import com.crm.dto.SalesOpportunityDTO;
import com.crm.dto.ScheduleConfigDTO;
import com.crm.dto.ValidationErrorResponseDTO;
import com.crm.entities.ScheduleConfig;
import com.crm.enums.SalesStage;
import com.crm.exception.InvalidCronExpressionException;
import com.crm.exception.InvalidDateTimeException;
import com.crm.exception.InvalidOpportunityIdException;
import com.crm.exception.InvalidSalesDetailsException;
import com.crm.scheduler.DynamicSchedulerService;
import com.crm.scheduler.DynamicSchedulerServiceImpl;
import com.crm.service.SalesOpportunityService;
import com.crm.service.SalesOpportunityServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@Slf4j
@WebMvcTest(controllers = SalesOpportunityController.class)
class SalesOpportunityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SalesOpportunityService service;

    @MockitoBean
    private DynamicSchedulerService schedulerService;

    @Autowired
    private ObjectMapper objectMapper;

    private static List<SalesOpportunityDTO> salesOpportunityDTOList;

    @BeforeEach
    void setup(){
        salesOpportunityDTOList = new ArrayList<>();
        SalesOpportunityDTO obj1 = SalesOpportunityDTO.builder().opportunityID(1L).customerID(1L).salesStage(SalesStage.QUALIFICATION).estimatedValue(new BigDecimal("10000.0")).closingDate(LocalDate.of(2025, Month.APRIL, 20)).build();
        SalesOpportunityDTO obj2 = SalesOpportunityDTO.builder().opportunityID(2L).customerID(2L).salesStage(SalesStage.PROSPECTING).estimatedValue(new BigDecimal("20000.0")).closingDate(LocalDate.of(2025, Month.APRIL, 21)).build();
        SalesOpportunityDTO obj3 = SalesOpportunityDTO.builder().opportunityID(3L).customerID(3L).salesStage(SalesStage.CLOSED_WON).estimatedValue(new BigDecimal("30000.0")).closingDate(LocalDate.of(2025, Month.APRIL, 22)).build();
        SalesOpportunityDTO obj4 = SalesOpportunityDTO.builder().opportunityID(4L).customerID(4L).salesStage(SalesStage.CLOSED_LOST).estimatedValue(new BigDecimal("40000.0")).closingDate(LocalDate.of(2025, Month.APRIL, 23)).build();
        SalesOpportunityDTO obj5 = SalesOpportunityDTO.builder().opportunityID(5L).customerID(5L).salesStage(SalesStage.PROSPECTING).estimatedValue(new BigDecimal("50000.0")).closingDate(LocalDate.of(2025, Month.APRIL, 24)).build();
        SalesOpportunityDTO obj6 = SalesOpportunityDTO.builder().opportunityID(6L).customerID(6L).salesStage(SalesStage.PROSPECTING).estimatedValue(new BigDecimal("60000.0")).closingDate(LocalDate.of(2025, Month.APRIL, 25)).followUpReminder(LocalDate.of(2025, Month.APRIL, 25)).build();

        salesOpportunityDTOList.add(obj1);
        salesOpportunityDTOList.add(obj2);
        salesOpportunityDTOList.add(obj3);
        salesOpportunityDTOList.add(obj4);
        salesOpportunityDTOList.add(obj5);
        salesOpportunityDTOList.add(obj6);
    }



    @Test
    @DisplayName("GET /api/sales-opportunity ==> 200")
    void retrieveAllSalesOpportunities_POSITIVE() throws Exception {
    // api: GET /api/sales-opportunity ==> 200 : List<SalesOpportunityDTO>
        List<SalesOpportunityDTO> expected = salesOpportunityDTOList;

        when(service.retrieveAllSalesOpportunities()).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(get("/api/sales-opportunity"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<SalesOpportunityDTO> actual = objectMapper.readValue(jsonResponse, new TypeReference<List<SalesOpportunityDTO>>() {});
        assertEquals(expected, actual);
        verify(service, times(1)).retrieveAllSalesOpportunities();
    }

    @Test
    @DisplayName("GET /api/sales-opportunity ==> 404")
    void retrieveAllSalesOpportunities_NEGATIVE() throws Exception {
    // api: GET /api/sales-opportunity ==> 404 : ErrorResponseDTO
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("404")
                .timestamp(LocalDateTime.now())
                .path("uri=/api/sales-opportunity")
                .message("No Sales Opportunity Available")
                .build();

        when(service.retrieveAllSalesOpportunities()).thenThrow(new NoSuchElementException("No Sales Opportunity Available"));

        MvcResult mvcResult = mockMvc.perform(get("/api/sales-opportunity"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getPath(), actual.getPath());
        verify(service, times(1)).retrieveAllSalesOpportunities();
    }


    @Test
    @DisplayName("POST /api/sales-opportunity ==> 200")
    void createSalesOpportunity_POSITIVE() throws Exception {
        // api: POST /api/sales-opportunity ==> 200 : SalesOpportunityDTO
        SalesOpportunityDTO expected = salesOpportunityDTOList.getFirst();

        when(service.createSalesOpportunity(any(SalesOpportunityDTO.class))).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(post("/api/sales-opportunity")
                .content("{\"customerID\":1,\"" +
                        "salesStage\":\"QUALIFICATION\"" +
                        ",\"estimatedValue\":10000.0" +
                        ",\"closingDate\":\"2025-04-20\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        SalesOpportunityDTO actual = objectMapper.readValue(jsonResponse, SalesOpportunityDTO.class);
        assertEquals(expected, actual);
        verify(service, times(1)).createSalesOpportunity(SalesOpportunityDTO.builder().customerID(1L).salesStage(SalesStage.QUALIFICATION).estimatedValue(new BigDecimal("10000.0")).closingDate(LocalDate.of(2025, Month.APRIL, 20)).build());
    }

    @Test
    @DisplayName("POST /api/sales-opportunity ==> 400")
    void createSalesOpportunity_NEGATIVE() throws Exception {
        // api: POST /api/sales-opportunity ==> 404 : ErrorResponseDTO
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("400")
                .timestamp(LocalDateTime.now())
                .path("uri=/api/sales-opportunity")
                .message("Invalid Details Provided")
                .build();

        when(service.createSalesOpportunity(any(SalesOpportunityDTO.class))).thenThrow(new InvalidSalesDetailsException("Invalid Details Provided"));

        MvcResult mvcResult = mockMvc.perform(post("/api/sales-opportunity")
                .content("{\"customerID\":1,\"" +
                        "salesStage\":\"QUALIFICATION\"" +
                        ",\"estimatedValue\":10000.0" +
                        ",\"closingDate\":\"2025-04-20\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getPath(), actual.getPath());
        verify(service, times(1)).createSalesOpportunity(SalesOpportunityDTO.builder().customerID(1L).salesStage(SalesStage.QUALIFICATION).estimatedValue(new BigDecimal("10000.0")).closingDate(LocalDate.of(2025, Month.APRIL, 20)).build());
    }


    @Test
    @DisplayName("GET /api/sales-opportunity/{opportunityID} ==> 200")
    void getOpportunitiesByOpportunity_POSITIVE() throws Exception {
        // api: GET /api/sales-opportunity/{opportunityID} ==> 200 : List<SalesOpportunityDTO>
        SalesOpportunityDTO expected = salesOpportunityDTOList.getFirst();

        when(service.getOpportunitiesByOpportunity(anyLong())).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(get("/api/sales-opportunity/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        SalesOpportunityDTO actual = objectMapper.readValue(jsonResponse, SalesOpportunityDTO.class);
        assertEquals(expected, actual);
        verify(service, times(1)).getOpportunitiesByOpportunity(1L);
    }

    @Test
    @DisplayName("GET /api/sales-opportunity/{opportunityID} ==> 404")
    void getOpportunitiesByOpportunity_NEGATIVE() throws Exception {
        // api: GET /api/sales-opportunity/{opportunityID} ==> 404 : List<SalesOpportunityDTO>
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("404")
                .timestamp(LocalDateTime.now())
                .path("uri=/api/sales-opportunity/1")
                .message("No leads found with given Opportunity ID")
                .build();

        when(service.getOpportunitiesByOpportunity(anyLong())).thenThrow(new NoSuchElementException("No leads found with given Opportunity ID"));

        MvcResult mvcResult = mockMvc.perform(get("/api/sales-opportunity/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getPath(), actual.getPath());
        verify(service, times(1)).getOpportunitiesByOpportunity(1L);
    }


    @Test
    @DisplayName("GET /api/sales-opportunity/customer/{customerID} ==> 200")
    void getOpportunitiesByCustomer_POSITIVE() throws Exception {
        // api: GET /api/sales-opportunity/customer/{customerID} ==> 200 : List<SalesOpportunityDTO>
        List<SalesOpportunityDTO> allResult = salesOpportunityDTOList;
        List<SalesOpportunityDTO> expected = new ArrayList<>();
        for(SalesOpportunityDTO obj : allResult){
            if(obj.getCustomerID() == 1L){
                expected.add(obj);
            }
        }

        when(service.getOpportunitiesByCustomer(anyLong())).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(get("/api/sales-opportunity/customer/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<SalesOpportunityDTO> actual = objectMapper.readValue(jsonResponse, new TypeReference<List<SalesOpportunityDTO>>() {});
        assertEquals(expected, actual);
        verify(service, times(1)).getOpportunitiesByCustomer(1L);
    }

    @Test
    @DisplayName("GET /api/sales-opportunity/customer/{customerID} ==> 404")
    void getOpportunitiesByCustomer_NEGATIVE() throws Exception {
        // api: GET /api/sales-opportunity/customer/{customerID} ==> 404 : ErrorResponseDTO
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("404")
                .timestamp(LocalDateTime.now())
                .path("uri=/api/sales-opportunity/customer/1")
                .message("No leads found with given Customer ID")
                .build();


        when(service.getOpportunitiesByCustomer(anyLong())).thenThrow(new NoSuchElementException("No leads found with given Customer ID"));

        MvcResult mvcResult = mockMvc.perform(get("/api/sales-opportunity/customer/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getPath(), actual.getPath());
        verify(service, times(1)).getOpportunitiesByCustomer(1L);
    }


    @Test
    @DisplayName("GET /api/sales-opportunity/salesStage/{salesStage} ==> 200")
    void getOpportunitiesBySalesStage_POSTIVE() throws Exception {
        // api: GET /api/sales-opportunity/salesStage/{salesStage} ==> 200 : List<SalesOpportunityDTO>
        List<SalesOpportunityDTO> allResult = salesOpportunityDTOList;
        List<SalesOpportunityDTO> expected = new ArrayList<>();
        for(SalesOpportunityDTO obj : allResult){
            if(obj.getSalesStage() == SalesStage.QUALIFICATION){
                expected.add(obj);
            }
        }

        when(service.getOpportunitiesBySalesStage(any(SalesStage.class))).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(get("/api/sales-opportunity/salesStage/QUALIFICATION"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<SalesOpportunityDTO> actual = objectMapper.readValue(jsonResponse, new TypeReference<List<SalesOpportunityDTO>>() {});
        assertEquals(expected, actual);
        verify(service, times(1)).getOpportunitiesBySalesStage(SalesStage.QUALIFICATION);
    }


    @Test
    @DisplayName("GET /api/sales-opportunity/salesStage/{salesStage} ==> 404")
    void getOpportunitiesBySalesStage_NEGATIVE() throws Exception {
        // api: GET /api/sales-opportunity/salesStage/{salesStage} ==> 404 : ErrorResponseDTO
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("404")
                .timestamp(LocalDateTime.now())
                .path("uri=/api/sales-opportunity/salesStage/QUALIFICATION")
                .message("No leads found with requested Sales Stage")
                .build();

        when(service.getOpportunitiesBySalesStage(any(SalesStage.class))).thenThrow(new NoSuchElementException("No leads found with requested Sales Stage"));

        MvcResult mvcResult = mockMvc.perform(get("/api/sales-opportunity/salesStage/QUALIFICATION"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getPath(), actual.getPath());
        verify(service, times(1)).getOpportunitiesBySalesStage(SalesStage.QUALIFICATION);
    }


    @Test
    @DisplayName("GET /api/sales-opportunity/estimatedValue/{estimatedValue} ==> 200")
    void getOpportunitiesByEstimatedValue_POSITIVE() throws Exception {
        // api: GET /api/sales-opportunity/estimatedValue/{estimatedValue} ==> 200 : List<SalesOpportunityDTO>
        List<SalesOpportunityDTO> allResult = salesOpportunityDTOList;
        List<SalesOpportunityDTO> expected = new ArrayList<>();
        for(SalesOpportunityDTO obj : allResult){
            if(Objects.equals(obj.getEstimatedValue(), new BigDecimal("10000.0"))){
                expected.add(obj);
            }
        }

        when(service.getOpportunitiesByEstimatedValue(any(BigDecimal.class))).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(get("/api/sales-opportunity/estimatedValue/10000.0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<SalesOpportunityDTO> actual = objectMapper.readValue(jsonResponse, new TypeReference<List<SalesOpportunityDTO>>() {});
        assertEquals(expected, actual);
        verify(service, times(1)).getOpportunitiesByEstimatedValue(new BigDecimal("10000.0"));
    }


    @Test
    @DisplayName("GET /api/sales-opportunity/estimatedValue/{estimatedValue} ==> 404")
    void getOpportunitiesByEstimatedValue_NEGATIVE() throws Exception {
        // api: GET /api/sales-opportunity/estimatedValue/{estimatedValue} ==> 404 : ErrorResponseDTO
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("404")
                .timestamp(LocalDateTime.now())
                .path("uri=/api/sales-opportunity/estimatedValue/10000.0")
                .message("No leads found with given Estimated Value")
                .build();

        when(service.getOpportunitiesByEstimatedValue(any(BigDecimal.class))).thenThrow(new NoSuchElementException("No leads found with given Estimated Value"));

        MvcResult mvcResult = mockMvc.perform(get("/api/sales-opportunity/estimatedValue/10000.0"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getPath(), actual.getPath());
        verify(service, times(1)).getOpportunitiesByEstimatedValue(new BigDecimal("10000.0"));
    }


    @Test
    @DisplayName("GET /api/sales-opportunity/closingDate/{closingDate} ==> 200")
    void getOpportunitiesByClosingDate_POSITIVE() throws Exception {
        // api: GET /api/sales-opportunity/closingDate/{closingDate} ==> 200 : List<SalesOpportunityDTO>
        List<SalesOpportunityDTO> allResult = salesOpportunityDTOList;
        List<SalesOpportunityDTO> expected = new ArrayList<>();
        for(SalesOpportunityDTO obj : allResult){
            if(Objects.equals(obj.getClosingDate(), LocalDate.of(2025, Month.APRIL, 20))){
                expected.add(obj);
            }
        }

        when(service.getOpportunitiesByClosingDate(any(LocalDate.class))).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(get("/api/sales-opportunity/closingDate/2025-04-20"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<SalesOpportunityDTO> actual = objectMapper.readValue(jsonResponse, new TypeReference<List<SalesOpportunityDTO>>() {});
        assertEquals(expected, actual);
        verify(service, times(1)).getOpportunitiesByClosingDate(LocalDate.of(2025, Month.APRIL, 20));
    }

    @Test
    @DisplayName("GET /api/sales-opportunity/closingDate/{closingDate} ==> 404")
    void getOpportunitiesByClosingDate_NEGATIVE() throws Exception {
        // api: GET /api/sales-opportunity/closingDate/{closingDate} ==> 404 : ErrorResponseDTO
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("404")
                .timestamp(LocalDateTime.now())
                .path("uri=/api/sales-opportunity/closingDate/2025-04-20")
                .message("No leads found with given Closing Date")
                .build();

        when(service.getOpportunitiesByClosingDate(any(LocalDate.class))).thenThrow(new NoSuchElementException("No leads found with given Closing Date"));

        MvcResult mvcResult = mockMvc.perform(get("/api/sales-opportunity/closingDate/2025-04-20"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getPath(), actual.getPath());
        verify(service, times(1)).getOpportunitiesByClosingDate(LocalDate.of(2025, Month.APRIL, 20));
    }


    @Test
    @DisplayName("GET /api/sales-opportunity/followUpReminder/{followUpReminder} ==> 200")
    void getOpportunitiesByFollowUpReminder_POSITIVE() throws Exception {
        // api: GET /api/sales-opportunity/followUpReminder/{followUpReminder} ==> 200 : List<SalesOpportunityDTO>
        List<SalesOpportunityDTO> allResult = salesOpportunityDTOList;
        List<SalesOpportunityDTO> expected = new ArrayList<>();
        for(SalesOpportunityDTO obj : allResult){
            if(Objects.equals(obj.getFollowUpReminder(), LocalDate.of(2025, Month.APRIL, 20))){
                expected.add(obj);
            }
        }

        when(service.getOpportunitiesByFollowUpReminder(any(LocalDate.class))).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(get("/api/sales-opportunity/followUpReminder/2025-04-20"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<SalesOpportunityDTO> actual = objectMapper.readValue(jsonResponse, new TypeReference<List<SalesOpportunityDTO>>() {});
        assertEquals(expected, actual);
        verify(service, times(1)).getOpportunitiesByFollowUpReminder(LocalDate.of(2025, Month.APRIL, 20));
    }

    @Test
    @DisplayName("GET /api/sales-opportunity/followUpReminder/{followUpReminder} ==> 404")
    void getOpportunitiesByFollowUpReminder_NEGATIVE() throws Exception {
        // api: GET /api/sales-opportunity/followUpReminder/{followUpReminder} ==> 404 : ErrorResponseDTO
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("404")
                .timestamp(LocalDateTime.now())
                .path("uri=/api/sales-opportunity/followUpReminder/2025-04-20")
                .message("No leads found with given Follow-up Reminder")
                .build();

        when(service.getOpportunitiesByFollowUpReminder(any(LocalDate.class))).thenThrow(new NoSuchElementException("No leads found with given Follow-up Reminder"));

        MvcResult mvcResult = mockMvc.perform(get("/api/sales-opportunity/followUpReminder/2025-04-20"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getPath(), actual.getPath());
        verify(service, times(1)).getOpportunitiesByFollowUpReminder(LocalDate.of(2025, Month.APRIL, 20));
    }


    @Test
    @DisplayName("POST /api/followUpReminder ==> 200")
    void scheduleFollowUpReminder_POSITIVE() throws Exception {
        // api: POST /api/followUpReminder ==> 200 : SalesOpportunityDTO
        SalesOpportunityDTO expected = salesOpportunityDTOList.getLast();

        when(service.scheduleFollowUpReminder(anyLong(), any(LocalDate.class))).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(post("/api/sales-opportunity/followUpReminder").param("opportunityId","1").param("reminderDate", "2025-02-20"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        SalesOpportunityDTO actual = objectMapper.readValue(jsonResponse, SalesOpportunityDTO.class);
        assertEquals(expected, actual);
        verify(service, times(1)).scheduleFollowUpReminder(1L, LocalDate.of(2025, Month.FEBRUARY, 20));
    }

    @Test
    @DisplayName("POST /api/followUpReminder ==> 400, InvalidOpportunityIdException")
    void scheduleFollowUpReminder_NEGATIVE_InvalidOpportunityIdException() throws Exception{
        // api: POST /api/followUpReminder ==> 400 : ErrorResponseDTO
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("400")
                .timestamp(LocalDateTime.now())
                .path("uri=/api/sales-opportunity/followUpReminder")
                .message("Lead with Opportunity ID 1 does not exist.")
                .build();

        when(service.scheduleFollowUpReminder(anyLong(), any(LocalDate.class))).thenThrow(new InvalidOpportunityIdException("Lead with Opportunity ID 1 does not exist."));

        MvcResult mvcResult = mockMvc.perform(post("/api/sales-opportunity/followUpReminder").param("opportunityId","1").param("reminderDate", "2025-02-20"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getPath(), actual.getPath());
        verify(service, times(1)).scheduleFollowUpReminder(1L, LocalDate.of(2025, Month.FEBRUARY, 20));
    }


    @Test
    @DisplayName("POST /api/followUpReminder ==> 400, InvalidDateTimeException")
    void scheduleFollowUpReminder_NEGATIVE_InvalidDateTimeException() throws Exception{
        // api: POST /api/followUpReminder ==> 400 : ErrorResponseDTO
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("400")
                .timestamp(LocalDateTime.now())
                .path("uri=/api/sales-opportunity/followUpReminder")
                .message("Please enter valid date")
                .build();

        when(service.scheduleFollowUpReminder(anyLong(), any(LocalDate.class))).thenThrow(new InvalidDateTimeException("Please enter valid date"));

        MvcResult mvcResult = mockMvc.perform(post("/api/sales-opportunity/followUpReminder").param("opportunityId","1").param("reminderDate", "2025-02-20"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getPath(), actual.getPath());
        verify(service, times(1)).scheduleFollowUpReminder(1L, LocalDate.of(2025, Month.FEBRUARY, 20));
    }


    @Test
    @DisplayName("DELETE /api/sales-opportunity ==> 200")
    void deleteByOpportunityID_POSITIVE() throws Exception {
        // api: DELETE /api/sales-opportunity ==> 200 : String
        Map<String, String> expected = new HashMap<>();
        expected.put("message", "Successfully deleted Lead with ID 1");

        when(service.deleteByOpportunityID(1L)).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(delete("/api/sales-opportunity/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        Map<String, String> actual = objectMapper.readValue(jsonResponse, Map.class);
        assertEquals(expected, actual);
        verify(service, times(1)).deleteByOpportunityID(1L);
    }

    @Test
    @DisplayName("DELETE /api/sales-opportunity ==> 400")
    void deleteByOpportunityID_NEGATIVE() throws Exception {
        // api: DELETE /api/sales-opportunity ==> 400 : ErrorResponseDTO
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("400")
                .timestamp(LocalDateTime.now())
                .path("uri=/api/sales-opportunity/1")
                .message("Lead with Opportunity ID 1 does not exist.")
                .build();

        when(service.deleteByOpportunityID(1L)).thenThrow( new InvalidOpportunityIdException("Lead with Opportunity ID 1 does not exist."));

        MvcResult mvcResult = mockMvc.perform(delete("/api/sales-opportunity/1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getPath(), actual.getPath());
        verify(service, times(1)).deleteByOpportunityID(1L);
    }

    @Test
    @DisplayName("DELETE /api/sales-opportunity ==> 500")
    void deleteByOpportunityID_NEGATIVE_FAILS() throws Exception {
        // api: DELETE /api/sales-opportunity ==> 500 : ErrorResponseDTO
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("500")
                .timestamp(LocalDateTime.now())
                .path("uri=/api/sales-opportunity/1")
                .message("Some error occurred while deleting Lead with ID 1")
                .build();

        when(service.deleteByOpportunityID(1L)).thenReturn(false);

        MvcResult mvcResult = mockMvc.perform(delete("/api/sales-opportunity/1"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getPath(), actual.getPath());
        verify(service, times(1)).deleteByOpportunityID(1L);
    }

    @Test
    @DisplayName("POST /api/sales-opportunity/configureCron ==> 200")
    void configCronJob_POSITIVE() throws Exception {
        ScheduleConfigDTO scheduleConfigDTO = ScheduleConfigDTO.builder()
                .cronExpression("* * * * * *")
                .build();

        ScheduleConfigDTO expected = ScheduleConfigDTO.builder()
                .cronExpression("* * * * * *")
                .taskName("Send Reminder")
                .id(1L)
                .build();


        when(schedulerService.updateCronExpression(scheduleConfigDTO)).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(post("/api/sales-opportunity/configureCron")
                .content("{\"cronExpression\":\"* * * * * *\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ScheduleConfigDTO actual = objectMapper.readValue(jsonResponse, ScheduleConfigDTO.class);
        assertEquals(expected, actual);
        verify(schedulerService, times(1)).updateCronExpression(scheduleConfigDTO);
    }
    @Test
    @DisplayName("POST /api/sales-opportunity/configureCron ==> 400")
    void configCronJob_NEGATIVE() throws Exception {
        ScheduleConfigDTO scheduleConfigDTO = ScheduleConfigDTO.builder()
                .cronExpression("a 2 c aa 2 1")
                .build();

        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .code("400")
                .message("Invalid Cron Expression "+ scheduleConfigDTO.getCronExpression())
                .path("uri=/api/sales-opportunity/configureCron")
                .build();


        when(schedulerService.updateCronExpression(scheduleConfigDTO)).thenThrow(new InvalidCronExpressionException("Invalid Cron Expression "+ scheduleConfigDTO.getCronExpression()));

        MvcResult mvcResult = mockMvc.perform(post("/api/sales-opportunity/configureCron")
                .content("{\"cronExpression\":\"a 2 c aa 2 1\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getPath(), actual.getPath());
        assertEquals(expected.getMessage(), actual.getMessage());
        verify(schedulerService, times(1)).updateCronExpression(scheduleConfigDTO);
    }

    @Test
    @DisplayName("POST /INVALID-BODY ==> 400")
    void ValidationFailed() throws Exception {
        ValidationErrorResponseDTO expected = ValidationErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .messages(List.of("Cron expression cannot be blank"))
                .path("uri=/api/sales-opportunity/configureCron")
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/api/sales-opportunity/configureCron")
                .content("{\"cronExpression\":\"\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ValidationErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ValidationErrorResponseDTO.class);
        log.info(actual.toString());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getPath(), actual.getPath());
        assertEquals(expected.getMessages(), actual.getMessages());
        verify(schedulerService, times(0)).updateCronExpression(any(ScheduleConfigDTO.class));
    }

}