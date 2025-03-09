package com.crm.controller;

import com.crm.dto.ErrorResponseDTO;
import com.crm.dto.SalesOpportunityDTO;
import com.crm.enums.SalesStage;
import com.crm.exception.InvalidDateTimeException;
import com.crm.exception.InvalidOpportunityIdException;
import com.crm.exception.InvalidSalesDetailsException;
import com.crm.service.SalesOpportunityServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SalesOpportunityController.class)
class SalesOpportunityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SalesOpportunityServiceImpl service;

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
        SalesOpportunityDTO obj6 = SalesOpportunityDTO.builder().opportunityID(6L).customerID(6L).salesStage(SalesStage.PROSPECTING).estimatedValue(new BigDecimal("60000.0")).closingDate(LocalDate.of(2025, Month.APRIL, 25)).followUpReminder(LocalDate.of(2025, Month.APRIL, 25).atStartOfDay()).build();

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
    }

    @Test
    @DisplayName("GET /api/sales-opportunity ==> 404")
    void retrieveAllSalesOpportunities_NEGATIVE() throws Exception {
    // api: GET /api/sales-opportunity ==> 404 : ErrorResponseDTO
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("404")
                .message("No Sales Opportunity Available")
                .build();

        when(service.retrieveAllSalesOpportunities()).thenThrow(new NoSuchElementException("No Sales Opportunity Available"));

        MvcResult mvcResult = mockMvc.perform(get("/api/sales-opportunity"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected, actual);
    }


    @Test
    @DisplayName("POST /api/sales-opportunity ==> 200")
    void createSalesOpportunity_POSITIVE() throws Exception {
        // api: POST /api/sales-opportunity ==> 200 : SalesOpportunityDTO
        SalesOpportunityDTO expected = salesOpportunityDTOList.getFirst();

        when(service.createSalesOpportunity(any(SalesOpportunityDTO.class))).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(post("/api/sales-opportunity")
                .content("{\"customerId\":1,\"" +
                        "salesStage\":\"QUALIFICATION\"" +
                        ",\"estimatedValue\":10000.0" +
                        ",\"closingDate\":\"2025-04-20\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        SalesOpportunityDTO actual = objectMapper.readValue(jsonResponse, SalesOpportunityDTO.class);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("POST /api/sales-opportunity ==> 400")
    void createSalesOpportunity_NEGATIVE() throws Exception {
        // api: POST /api/sales-opportunity ==> 404 : ErrorResponseDTO
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("400")
                .message("Invalid Details Provided")
                .build();

        when(service.createSalesOpportunity(any(SalesOpportunityDTO.class))).thenThrow(new InvalidSalesDetailsException("Invalid Details Provided"));

        MvcResult mvcResult = mockMvc.perform(post("/api/sales-opportunity")
                .content("{\"customerId\":1,\"" +
                        "salesStage\":\"QUALIFICATION\"" +
                        ",\"estimatedValue\":10000.0" +
                        ",\"closingDate\":\"2025-04-20\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected, actual);
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
    }

    @Test
    @DisplayName("GET /api/sales-opportunity/{opportunityID} ==> 404")
    void getOpportunitiesByOpportunity_NEGATIVE() throws Exception {
        // api: GET /api/sales-opportunity/{opportunityID} ==> 404 : List<SalesOpportunityDTO>
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("404")
                .message("No leads found with given Opportunity ID")
                .build();

        when(service.getOpportunitiesByOpportunity(anyLong())).thenThrow(new NoSuchElementException("No leads found with given Customer ID"));

        MvcResult mvcResult = mockMvc.perform(get("/api/sales-opportunity/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected, actual);
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
    }

    @Test
    @DisplayName("GET /api/sales-opportunity/customer/{customerID} ==> 404")
    void getOpportunitiesByCustomer_NEGATIVE() throws Exception {
        // api: GET /api/sales-opportunity/customer/{customerID} ==> 404 : ErrorResponseDTO
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("404")
                .message("No leads found with given Customer ID")
                .build();


        when(service.getOpportunitiesByCustomer(anyLong())).thenThrow(new NoSuchElementException("No leads found with given Customer ID"));

        MvcResult mvcResult = mockMvc.perform(get("/api/sales-opportunity/customer/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected, actual);
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
    }


    @Test
    @DisplayName("GET /api/sales-opportunity/salesStage/{salesStage} ==> 404")
    void getOpportunitiesBySalesStage_NEGATIVE() throws Exception {
        // api: GET /api/sales-opportunity/salesStage/{salesStage} ==> 404 : ErrorResponseDTO
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("404")
                .message("No leads found with requested Sales Stage")
                .build();

        when(service.getOpportunitiesBySalesStage(any(SalesStage.class))).thenThrow(new NoSuchElementException("No leads found with requested Sales Stage"));

        MvcResult mvcResult = mockMvc.perform(get("/api/sales-opportunity/salesStage/QUALIFICATION"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected, actual);
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
    }


    @Test
    @DisplayName("api: GET /api/sales-opportunity/estimatedValue/{estimatedValue} ==> 404")
    void getOpportunitiesByEstimatedValue_NEGATIVE() throws Exception {
        // api: GET /api/sales-opportunity/estimatedValue/{estimatedValue} ==> 404 : ErrorResponseDTO
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("404")
                .message("No leads found with given Estimated Value")
                .build();

        when(service.getOpportunitiesByEstimatedValue(any(BigDecimal.class))).thenThrow(new NoSuchElementException("No leads found with given Estimated Value"));

        MvcResult mvcResult = mockMvc.perform(get("/api/sales-opportunity/estimatedValue/10000.0"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected, actual);
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
    }

    @Test
    @DisplayName("GET /api/sales-opportunity/closingDate/{closingDate} ==> 404")
    void getOpportunitiesByClosingDate_NEGATIVE() throws Exception {
        // api: GET /api/sales-opportunity/closingDate/{closingDate} ==> 404 : ErrorResponseDTO
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("404")
                .message("No leads found with given Closing Date")
                .build();

        when(service.getOpportunitiesByClosingDate(any(LocalDate.class))).thenThrow(new NoSuchElementException("No leads found with given Closing Date"));

        MvcResult mvcResult = mockMvc.perform(get("/api/sales-opportunity/closingDate/2025-04-20"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected, actual);
    }


    @Test
    @DisplayName("GET /api/sales-opportunity/followUpReminder/{followUpReminder} ==> 200")
    void getOpportunitiesByFollowUpReminder_POSITIVE() throws Exception {
        // api: GET /api/sales-opportunity/followUpReminder/{followUpReminder} ==> 200 : List<SalesOpportunityDTO>
        List<SalesOpportunityDTO> allResult = salesOpportunityDTOList;
        List<SalesOpportunityDTO> expected = new ArrayList<>();
        for(SalesOpportunityDTO obj : allResult){
            if(Objects.equals(obj.getClosingDate(), LocalDate.of(2025, Month.APRIL, 20))){
                expected.add(obj);
            }
        }

        when(service.getOpportunitiesByFollowUpReminder(any(LocalDateTime.class))).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(get("/api/sales-opportunity/followUpReminder/2025-04-20T00:00:00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<SalesOpportunityDTO> actual = objectMapper.readValue(jsonResponse, new TypeReference<List<SalesOpportunityDTO>>() {});
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("GET /api/sales-opportunity/followUpReminder/{followUpReminder} ==> 404")
    void getOpportunitiesByFollowUpReminder_NEGATIVE() throws Exception {
        // api: GET /api/sales-opportunity/followUpReminder/{followUpReminder} ==> 404 : ErrorResponseDTO
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("404")
                .message("No leads found with given Follow-up Reminder")
                .build();

        when(service.getOpportunitiesByFollowUpReminder(any(LocalDateTime.class))).thenThrow(new NoSuchElementException("No leads found with given Follow-up Reminder"));

        MvcResult mvcResult = mockMvc.perform(get("/api/sales-opportunity/followUpReminder/2025-04-20T00:00:00"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected, actual);
    }


    @Test
    @DisplayName("POST /api/followUpReminder ==> 200")
    void scheduleFollowUpReminder_POSITIVE() throws Exception {
        // api: POST /api/followUpReminder ==> 200 : SalesOpportunityDTO
        SalesOpportunityDTO expected = salesOpportunityDTOList.getLast();

        when(service.scheduleFollowUpReminder(anyLong(), any(LocalDateTime.class))).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(post("/api/followUpReminder?opportunityID=1&reminderDate=2025-04-20T00:00:00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        SalesOpportunityDTO actual = objectMapper.readValue(jsonResponse, SalesOpportunityDTO.class);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("POST /api/followUpReminder ==> 400, InvalidOpportunityIdException")
    void scheduleFollowUpReminder_NEGATIVE_InvalidOpportunityIdException() throws Exception{
        // api: POST /api/followUpReminder ==> 400 : ErrorResponseDTO
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("400")
                .message("Lead with Opportunity ID 1 does not exist.")
                .build();

        when(service.scheduleFollowUpReminder(anyLong(), any(LocalDateTime.class))).thenThrow(new InvalidOpportunityIdException("Lead with Opportunity ID 1 does not exist."));

        MvcResult mvcResult = mockMvc.perform(post("/api/followUpReminder?opportunityID=1&reminderDate=2025-04-20T00:00:00"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected, actual);
    }


    @Test
    @DisplayName("POST /api/followUpReminder ==> 400, InvalidDateTimeException")
    void scheduleFollowUpReminder_NEGATIVE_InvalidDateTimeException() throws Exception{
        // api: POST /api/followUpReminder ==> 400 : ErrorResponseDTO
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("400")
                .message("Please enter valid date")
                .build();

        when(service.scheduleFollowUpReminder(anyLong(), any(LocalDateTime.class))).thenThrow(new InvalidDateTimeException("Please enter valid date"));

        MvcResult mvcResult = mockMvc.perform(post("/api/followUpReminder?opportunityID=1&reminderDate=2025-04-20T00:00:00"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected, actual);
    }


    @Test
    @DisplayName("DELETE /api/sales-opportunity ==> 200")
    void deleteByOpportunityID_POSITIVE() throws Exception {
        // api: DELETE /api/sales-opportunity ==> 200 : String
        String expected = "Successfully deleted Lead with ID 1";

        when(service.deleteByOpportunityID(1L)).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(post("/api/followUpReminder?opportunityID=1&reminderDate=2025-04-20T00:00:00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        String actual = objectMapper.readValue(jsonResponse, String.class);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("DELETE /api/sales-opportunity ==> 400")
    void deleteByOpportunityID_NEGATIVE() throws Exception {
        // api: DELETE /api/sales-opportunity ==> 400 : ErrorResponseDTO
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("400")
                .message("Lead with Opportunity ID 1 does not exist.")
                .build();

        when(service.deleteByOpportunityID(1L)).thenThrow( new InvalidOpportunityIdException("Lead with Opportunity ID 1 does not exist."));

        MvcResult mvcResult = mockMvc.perform(post("/api/followUpReminder?opportunityID=1&reminderDate=2025-04-20T00:00:00"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected, actual);
    }


}