package com.crm.serviceTestCase;

import com.crm.dto.SupportTicketDTO;
import com.crm.entities.SupportTicket;
import com.crm.enums.Status;
import com.crm.exception.InvalidTicketIdException;
import com.crm.exception.InvalidTicketDetailsException;
import com.crm.mapper.SupportTicketMapper;
import com.crm.repository.SupportTicketRepository;
import com.crm.service.SupportTicketServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SupportTicketServiceImplTest {

    @InjectMocks
    private SupportTicketServiceImpl service;

    @Mock
    private SupportTicketRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("retrieveAllSupportTickets()-positive")
    void retrieveAllSupportTickets_ShouldReturnList_WhenTicketsExist() {
        List<SupportTicket> tickets = Arrays.asList(
            new SupportTicket(),
            new SupportTicket()
        );
        when(repository.findAll()).thenReturn(tickets);

        List<SupportTicketDTO> result = service.retrieveAllSupportTickets();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("retrieveAllSupportTickets()-negative")
    void retrieveAllSupportTickets_ShouldThrowException_WhenNoTicketsExist() {
        when(repository.findAll()).thenReturn(Arrays.asList());

        assertThrows(NoSuchElementException.class, () -> service.retrieveAllSupportTickets());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("createSupportTicket()-positive")
    void createSupportTicket_ShouldReturnTicket_WhenValidInput() {
        SupportTicket ticket = new SupportTicket();
        SupportTicketDTO dto = SupportTicketMapper.MAPPER.mapToDTO(ticket);

        when(repository.save(any(SupportTicket.class))).thenReturn(ticket);

        SupportTicketDTO result = service.createSupportTicket(dto);

        assertNotNull(result);
        verify(repository, times(1)).save(any(SupportTicket.class));
    }

    @Test
    @DisplayName("createSupportTicket()-negative")
    void createSupportTicket_ShouldThrowException_WhenInvalidInput() {
        when(repository.save(any(SupportTicket.class))).thenThrow(RuntimeException.class);
        SupportTicketDTO dto = new SupportTicketDTO();

        assertThrows(InvalidTicketDetailsException.class, () -> service.createSupportTicket(dto));
        verify(repository, times(1)).save(any(SupportTicket.class));
    }

    @Test
    @DisplayName("getSupportTicketById()-positive")
    void getSupportTicketById_ShouldReturnTicket_WhenIdExists() {
        SupportTicket ticket = new SupportTicket();
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));

        SupportTicketDTO result = service.getSupportTicketById(1L);
        assertNotNull(result);
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getSupportTicketById()-negative")
    void getSupportTicketById_ShouldThrowException_WhenIdDoesNotExist() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.getSupportTicketById(1L));
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getSupportTicketsByCustomer()-positive")
    void getSupportTicketsByCustomer_ShouldReturnList_WhenTicketsExist() {
        List<SupportTicket> tickets = Arrays.asList(
            new SupportTicket(),
            new SupportTicket()
        );
        when(repository.findByCustomerID(101L)).thenReturn(tickets);

        List<SupportTicketDTO> result = service.getSupportTicketsByCustomer(101L);

        assertEquals(2, result.size());
        verify(repository, times(1)).findByCustomerID(101L);
    }

    @Test
    @DisplayName("getSupportTicketsByCustomer()-negative")
    void getSupportTicketsByCustomer_ShouldThrowException_WhenNoTicketsExist() {
        when(repository.findByCustomerID(101L)).thenReturn(Arrays.asList());

        assertThrows(NoSuchElementException.class, () -> service.getSupportTicketsByCustomer(101L));
        verify(repository, times(1)).findByCustomerID(101L);
    }

    @Test
    @DisplayName("updateTicketStatus()-positive")
    void updateTicketStatus_ShouldUpdateStatus_WhenTicketExists() {
        SupportTicket ticket = new SupportTicket();
        ticket.setTicketID(1L);
        ticket.setStatus(Status.OPEN);
        ticket.setCustomerID(1L);
        ticket.setAssignedAgent(1L);
        ticket.setIssueDescription("xyz");
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));
        ticket.setStatus(Status.CLOSED);
        when(repository.save(any())).thenReturn(ticket);

        SupportTicketDTO result = service.updateTicketStatus(1L, Status.CLOSED);
        assertEquals(Status.CLOSED, result.getStatus());
        verify(repository, times(1)).save(any(SupportTicket.class));
    }

    @Test
    @DisplayName("updateTicketStatus()-negative")
    void updateTicketStatus_ShouldThrowException_WhenTicketDoesNotExist() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InvalidTicketIdException.class, () -> service.updateTicketStatus(1L, Status.CLOSED));
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("deleteSupportTicketById()-positive")
    void deleteSupportTicketById_ShouldReturnTrue_WhenTicketExists() {
        SupportTicket ticket = new SupportTicket();
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));

        boolean result = service.deleteSupportTicketById(1L);

        assertTrue(result);
        verify(repository, times(1)).delete(ticket);
    }

    @Test
    @DisplayName("deleteSupportTicketById()-negative")
    void deleteSupportTicketById_ShouldThrowException_WhenTicketDoesNotExist() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InvalidTicketIdException.class, () -> service.deleteSupportTicketById(1L));
        verify(repository, times(1)).findById(1L);
    }
}
