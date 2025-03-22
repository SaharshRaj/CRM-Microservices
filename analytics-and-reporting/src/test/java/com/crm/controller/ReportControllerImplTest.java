////package com.crm.controller;
////
////import com.crm.controller.ReportControllerImpl;
////import com.crm.dto.ErrorResponseDTO;
////import com.crm.entities.Report;
////import com.crm.service.ReportService;
////import org.junit.jupiter.api.Test;
////import org.mockito.Mockito;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
////import org.springframework.boot.test.mock.mockito.MockBean;
////import org.springframework.http.MediaType;
////import org.springframework.test.web.servlet.MockMvc;
////import org.springframework.test.web.servlet.MvcResult;
////import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
////import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
////import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
////import com.fasterxml.jackson.databind.ObjectMapper;
////import java.time.LocalDateTime;
////import java.util.Arrays;
////import java.util.List;
////
////import static org.junit.jupiter.api.Assertions.assertEquals;
////
////@WebMvcTest(ReportControllerImpl.class)
////public class ReportControllerImplTest {
////
////    @Autowired
////    private MockMvc mockMvc;
////
////    @Autowired
////    private ObjectMapper objectMapper;
////
////    @Test
////    public void testGetReportById() throws Exception {
////        String invalidId = "alll";
////
////        ErrorResponseDTO expected = ErrorResponseDTO.builder()
////                .code("400")
////                .timestamp(LocalDateTime.now())
////                .message("Method parameter 'id': Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; For input string: \"alll\"")
////                .path("uri=/api/analytics/" + invalidId)
////                .build();
////
////        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/analytics/" + invalidId))
////                .andDo(MockMvcResultHandlers.print())
////                .andExpect(MockMvcResultMatchers.status().isBadRequest())
////                .andReturn();
////
////        String jsonResponse = mvcResult.getResponse().getContentAsString();
////        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
////
////        assertEquals(expected.getCode(), actual.getCode());
////        assertEquals(expected.getMessage(), actual.getMessage());
////        assertEquals(expected.getPath(), actual.getPath());
////    }
////}
//
//package com.crm.controller;
//import com.crm.entities.Report;
//import com.crm.enums.ReportType;
//import com.crm.service.ReportService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
////import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//
//import java.util.Arrays;
//import java.util.List;
//
//@WebMvcTest(ReportControllerImpl.class)
//public class ReportControllerImplTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Mock
//    private ReportService reportService;
//
//    @Test
//    public void testGetAllAnalyticsEndpoint() throws Exception {
//        // Mocking the service layer's response
//        List<Report> mockReports = Arrays.asList(
//                new Report(1, "SALES", new dataPoints()),
//                new Report(3, "CUSTOMER", new DataPoints())
//        );
//        Mockito.when(reportService.getReportByType().thenReturn(mockReports));
//
//        // Performing the GET request to the endpoint
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/analytics/all"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[1].reportType").value("CUSTOMER"));
//    }
//
//    @Test
//    public void testGetAnalyticsByTypeEndpoint() throws Exception {
//        // Mocking the service layer's response
//        List<Report> mockReports = Arrays.asList(
//                new Report(5, "MARKETING", new DataPoints())
//        );
//        Mockito.when(reportService.getReportByType("MARKETING")).thenReturn(mockReports);
//
//        // Performing the GET request to the endpoint
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/analytics/type/MARKETING"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].reportType").value("MARKETING"));
//    }
//
//    @Test
//    public void testGetAnalyticsByTypeEndpointInvalidType() throws Exception {
//        // Mocking the service layer's response
//        Mockito.when(reportService.getReportByType(ReportType.valueOf(("INVALID"))).thenReturn(List.of()));
//
//        // Performing the GET request to the endpoint
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/analytics/type/INVALID"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
//    }
//
//    @Test
//    public void testGetAllAnalyticsEndpointError() throws Exception {
//        // Mocking the service layer to throw an exception
//        Mockito.when(reportService.getReportByType()).thenThrow(new RuntimeException("Database error"));
//
//        // Performing the GET request to the endpoint
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/analytics/all"))
//                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
//    }
//}
