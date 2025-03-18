package com.crm.service;

import com.crm.dto.SupportTicketDTO;
import com.crm.entities.SupportTicket;
import com.crm.enums.Status;
import com.crm.exception.InvalidTicketIdException;
import com.crm.exception.InvalidTicketDetailsException;
import com.crm.mapper.SupportTicketMapper;
import com.crm.repository.SupportTicketRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SupportTicketServiceImpl implements SupportTicketService {

    private SupportTicketRepository repository;
    
    public SupportTicketServiceImpl(SupportTicketRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<SupportTicketDTO> retrieveAllSupportTickets() throws NoSuchElementException {
        List<SupportTicket> tickets = repository.findAll();
        List<SupportTicketDTO> ticketDTOs = new ArrayList<>();
        tickets.forEach(ticket -> ticketDTOs.add(SupportTicketMapper.MAPPER.mapToDTO(ticket)));

        if (ticketDTOs.isEmpty()) {
            throw new NoSuchElementException("No support tickets available");
        }

        return ticketDTOs;
    }

    @Override
    public SupportTicketDTO createSupportTicket(SupportTicketDTO supportTicketDto) throws InvalidTicketDetailsException {
        SupportTicket ticket = SupportTicketMapper.MAPPER.mapToSupportTicket(supportTicketDto);
        try {
            SupportTicket savedTicket = repository.save(ticket);
            return SupportTicketMapper.MAPPER.mapToDTO(savedTicket);
        } catch (Exception e) {
            throw new InvalidTicketDetailsException(e.getMessage());
        }
    }

    @Override
    public SupportTicketDTO getSupportTicketById(Long ticketId) throws NoSuchElementException {
        Optional<SupportTicket> ticket = repository.findById(ticketId);
        if (ticket.isPresent()) {
            return SupportTicketMapper.MAPPER.mapToDTO(ticket.get());
        } else {
            throw new NoSuchElementException("No ticket found with the given ID");
        }
    }

    @Override
    public List<SupportTicketDTO> getSupportTicketsByCustomer(Long customerId) throws NoSuchElementException {
        List<SupportTicket> tickets = repository.findByCustomerID(customerId);
        if (tickets.isEmpty()) {
            throw new NoSuchElementException("No tickets found for the given customer ID");
        } else {
            List<SupportTicketDTO> ticketDTOs = new ArrayList<>();
            tickets.forEach(ticket -> ticketDTOs.add(SupportTicketMapper.MAPPER.mapToDTO(ticket)));
            return ticketDTOs;
        }
    }

    @Override
    public List<SupportTicketDTO> getSupportTicketsByStatus(Status status) throws NoSuchElementException {
        List<SupportTicket> tickets = repository.findByStatus(status);
        if (tickets.isEmpty()) {
            throw new NoSuchElementException("No tickets found with the given status");
        } else {
            List<SupportTicketDTO> ticketDTOs = new ArrayList<>();
            tickets.forEach(ticket -> ticketDTOs.add(SupportTicketMapper.MAPPER.mapToDTO(ticket)));
            return ticketDTOs;
        }
    }

    
    private static final String TICKET_NOT_FOUND_MESSAGE = "Ticket with the given ID does not exist";

    @Override
    public SupportTicketDTO updateTicketStatus(Long ticketId, Status status) throws InvalidTicketIdException {
        Optional<SupportTicket> ticket = repository.findById(ticketId);
        if (ticket.isPresent()) {
            SupportTicket existingTicket = ticket.get();
            existingTicket.setStatus(status); /**Update the ticket status*/
            /**Save and return the updated ticket as a DTO*/
            return SupportTicketMapper.MAPPER.mapToDTO(repository.save(existingTicket));
        } else {
            throw new InvalidTicketIdException(TICKET_NOT_FOUND_MESSAGE);
        }
    }

    @Override
    public SupportTicketDTO assignTicketToAgent(Long ticketId, Long agentId) throws InvalidTicketIdException {
        Optional<SupportTicket> ticket = repository.findById(ticketId);
        if (ticket.isPresent()) {
            SupportTicket existingTicket = ticket.get();
            existingTicket.setAssignedAgent(agentId);
            SupportTicket updatedTicket = repository.save(existingTicket);
            return SupportTicketMapper.MAPPER.mapToDTO(updatedTicket);
        } else {
            throw new InvalidTicketIdException(TICKET_NOT_FOUND_MESSAGE);
        }
    }

    @Override
    public boolean deleteSupportTicketById(Long ticketId) throws InvalidTicketIdException {
        Optional<SupportTicket> ticket = repository.findById(ticketId);
        if (ticket.isPresent()) {
            repository.delete(ticket.get());
            return true;
        } else {
            throw new InvalidTicketIdException(TICKET_NOT_FOUND_MESSAGE);
        }
    }

}
