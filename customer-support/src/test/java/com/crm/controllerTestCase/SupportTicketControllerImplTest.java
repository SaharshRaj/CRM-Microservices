package com.crm.controllerTestCase;

import com.crm.controller.SupportTicketControllerImpl;
import com.crm.dto.SupportTicketDTO;
import com.crm.enums.Status;
import com.crm.service.SupportTicketService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SupportTicketControllerImplTest {

    @Mock
    private SupportTicketService service;

    @InjectMocks
    private SupportTicketControllerImpl controller;
    
    /**
     * This method sets up the test environment by initializing the mocks
     * before each test case is executed. It ensures that the dependencies
     * of the class under test are properly mocked.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    /**
     * This test class verifies the functionality of the SupportTicketControllerImpl class, 
     * including methods for retrieving, creating, updating, assigning, and deleting support tickets. 
     * Each test case follows the Arrange-Act-Assert pattern:
     * - Arrange: Mock the behavior of the service layer.
     * - Act: Call the corresponding controller method being tested.
     * - Assert: Validate the response, ensure correctness of the results, and verify interaction 
     *   with the service layer using Mockito.
     * The tests ensure proper integration between the controller and service layers, 
     * covering all major functionalities of the Customer Support Module.
     */
    @Test
    @DisplayName("RetrieveAllTickets()-success")
    void testRetrieveAllSupportTickets_Success() {
    	//Arrange
        List<SupportTicketDTO> mockTickets = new ArrayList<>();
        mockTickets.add(new SupportTicketDTO());
        when(service.retrieveAllSupportTickets()).thenReturn(mockTickets);

        //Act
        ResponseEntity<List<SupportTicketDTO>> response = controller.retrieveAllSupportTickets();

        //Assert
        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        verify(service, times(1)).retrieveAllSupportTickets();
    }

    @Test
    @DisplayName("createSupportTicket()-success")
    void testCreateSupportTicket_Success() {
    	//Arrange
        SupportTicketDTO ticketDTO = new SupportTicketDTO();
        SupportTicketDTO savedTicketDTO = new SupportTicketDTO();
        when(service.createSupportTicket(ticketDTO)).thenReturn(savedTicketDTO);

        //Act
        ResponseEntity<SupportTicketDTO> response = controller.createSupportTicket(ticketDTO);

        //Assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        verify(service, times(1)).createSupportTicket(ticketDTO);
    }

    @Test
    @DisplayName("GetSupportTicketById()-success")
    void testGetSupportTicketById_Success() {
    	//Arrange
        SupportTicketDTO ticketDTO = new SupportTicketDTO();
        when(service.getSupportTicketById(1L)).thenReturn(ticketDTO);

        //Act
        ResponseEntity<SupportTicketDTO> response = controller.getSupportTicketById(1L);

        //Assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        verify(service, times(1)).getSupportTicketById(1L);
    }

    @Test
    @DisplayName("GetSupportTicketByCustomer()-success")
    void testGetSupportTicketsByCustomer_Success() {
    	//Arrange
        List<SupportTicketDTO> mockTickets = new ArrayList<>();
        mockTickets.add(new SupportTicketDTO());
        when(service.getSupportTicketsByCustomer(123L)).thenReturn(mockTickets);

        //Act
        ResponseEntity<List<SupportTicketDTO>> response = controller.getSupportTicketsByCustomer(123L);

        //Assert
        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        verify(service, times(1)).getSupportTicketsByCustomer(123L);
    }

    @Test
    @DisplayName("GetSupportTicketByStatus()-success")
    void testGetSupportTicketsByStatus_Success() {
    	//Arrange
        List<SupportTicketDTO> mockTickets = new ArrayList<>();
        mockTickets.add(new SupportTicketDTO());
        when(service.getSupportTicketsByStatus(Status.OPEN)).thenReturn(mockTickets);

        //Act
        ResponseEntity<List<SupportTicketDTO>> response = controller.getSupportTicketsByStatus(Status.OPEN);

        //Assert
        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        verify(service, times(1)).getSupportTicketsByStatus(Status.OPEN);
    }

    @Test
    @DisplayName("UpdateTicketStatus()-success")
    void testUpdateTicketStatus_Success() {
    	//Arrange
        SupportTicketDTO ticketDTO = new SupportTicketDTO();
        when(service.updateTicketStatus(1L, Status.CLOSED)).thenReturn(ticketDTO);

        //Act
        ResponseEntity<SupportTicketDTO> response = controller.updateTicketStatus(1L, Status.CLOSED);

        //Assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        verify(service, times(1)).updateTicketStatus(1L, Status.CLOSED);
    }

    @Test
    @DisplayName("AssignTicketToAgent()-success")
    void testAssignTicketToAgent_Success() {
        //Arrange
        SupportTicketDTO ticketDTO = new SupportTicketDTO();
        when(service.assignTicketToAgent(1L, 2L)).thenReturn(ticketDTO);

        //Act
        ResponseEntity<SupportTicketDTO> response = controller.assignTicketToAgent(1L, 2L);

        //Assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        verify(service, times(1)).assignTicketToAgent(1L, 2L);
    }

    @Test
    @DisplayName("DeleteSupportTicketById()-success")
    void testDeleteSupportTicketById_Success() {
        //Arrange
        when(service.deleteSupportTicketById(1L)).thenReturn(true);

        //Act
        ResponseEntity<String> response = controller.deleteSupportTicketById(1L);

        //Assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Successfully deleted"));
        verify(service, times(1)).deleteSupportTicketById(1L);
    }

}
