package com.crm.repositoryTestCase;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.crm.CustomerSupportApplication;
import com.crm.entities.SupportTicket;
import com.crm.enums.Status;
import com.crm.repository.SupportTicketRepository;

@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes= {CustomerSupportApplication.class})
class SupportTicketRepositoryTest {
	
	@Autowired
	private SupportTicketRepository supportTicketRepository;
	
    /**JUnit test cases for SupportTicket*/
	
	@Test
	@DisplayName("saveSupportTicket() - Positive")
	void testSaveSupportTicket_positive() {
		SupportTicket ticket = SupportTicket.builder()
				.customerID(1L)
				.issueDescription("The ticket has been opened")
				.assignedAgent(1L)
				.status(Status.OPEN)
				.build();
		
		SupportTicket savedSupportTicket = supportTicketRepository.save(ticket);
		assertTrue(savedSupportTicket.getTicketID() > 0 ,"Ticket ID should be greater than 0");
	}
	
	@Test
	@DisplayName("findSupportTicketById() - Positive")
	void findSupportTicketByIdTest_positive() {
		SupportTicket ticket = SupportTicket.builder()
				.customerID(1L)
				.issueDescription("The ticket has been opened")
				.assignedAgent(1L)
				.status(Status.OPEN)
				.build();
		
		SupportTicket savedSupportTicket = supportTicketRepository.save(ticket);
		Optional<SupportTicket> foundTicket = supportTicketRepository.findById(savedSupportTicket.getTicketID());
		assertTrue(foundTicket.isPresent(), "Support Ticket not found");
		assertEquals(savedSupportTicket.getTicketID(),foundTicket.get().getTicketID());
	}
	
	@Test
	@DisplayName("findSupportTicketById() - Negative")
	void findSupportTicketByIdTest_negative() {
		Optional<SupportTicket> savedSupportTicket = supportTicketRepository.findById(999L); 
		assertFalse(savedSupportTicket.isPresent(),"Support Ticket should not be found");
	}
	
	@Test
	@DisplayName("findAllSupportTicket() - Positive ")
	void findAllSupportTicket_positive() {
		SupportTicket ticket1 = SupportTicket.builder()
				.customerID(1L)
				.issueDescription("The ticket has been opened")
				.assignedAgent(1L)
				.status(Status.OPEN)
				.build();
		
		SupportTicket ticket2 = SupportTicket.builder()
				.customerID(2L)
				.issueDescription("The ticket has been closed")
				.assignedAgent(2L)
				.status(Status.CLOSED)
				.build();
		//Save the tickets
		supportTicketRepository.save(ticket1);
		supportTicketRepository.save(ticket2);
		
		List <SupportTicket> allTickets = supportTicketRepository.findAll();
		assertFalse(allTickets.isEmpty(),"SupportTicket list should not be empty");
		assertEquals(2, allTickets.size(),"Support Ticket should contain 2 entries");
	}
	
	@Test
	@DisplayName("findAllSupportTicket() - Negative")
	void findAllSupportTicket_negative() {
	List <SupportTicket> allTickets = supportTicketRepository.findAll();
	assertTrue(allTickets.isEmpty(),"Support Ticket list should be empty");
	}
	
	@Test
	@DisplayName("updateSupportTicket() - Positive")
	void updateSupportTicket_Positive() {
	    SupportTicket ticket = SupportTicket.builder()
	            .customerID(1L)
	            .issueDescription("The ticket has been opened")
	            .assignedAgent(1L)
	            .status(Status.OPEN)
	            .build();

	    //Save the ticket
	    SupportTicket savedTicket = supportTicketRepository.save(ticket);

	    //Retrieve and update the ticket
	    SupportTicket supportTicket = supportTicketRepository.findById(savedTicket.getCustomerID()).get();
	    supportTicket.setStatus(Status.CLOSED);
	    SupportTicket updatedTicket = supportTicketRepository.save(supportTicket);

	    //Add assertions
	    assertNotNull(updatedTicket);
	    assertEquals(Status.CLOSED, updatedTicket.getStatus());
	    assertEquals(savedTicket.getCustomerID(), updatedTicket.getCustomerID());
	}
	
	@Test
	@DisplayName("deleteSupportTicket() - Positive")
	void deleteSupportTicket_Positive() {
		SupportTicket ticket = SupportTicket.builder()
				.customerID(1L)
				.issueDescription("The ticket has been opened")
				.assignedAgent(1L)
				.status(Status.OPEN)
				.build();
		
		SupportTicket savedTicket = supportTicketRepository.save(ticket);
		
		supportTicketRepository.delete(savedTicket);
		Optional<SupportTicket> newSupportTicket = supportTicketRepository.findById(savedTicket.getTicketID()); 
		assertFalse(newSupportTicket.isPresent());
	}
	
	@Test
	@DisplayName("findSupportTicketByCustomers() - Positive")
	void findSupportTicketByCustomersTest_Positive() {
		SupportTicket ticket = SupportTicket.builder()
				.customerID(1L)
				.issueDescription("The ticket has been opened")
				.assignedAgent(1L)
				.status(Status.OPEN)
				.build();
		
		supportTicketRepository.save(ticket);
		
		List <SupportTicket> supportTicketList = supportTicketRepository.findByCustomerID(1L);
		assertFalse(supportTicketList.isEmpty(),"SupportTicket list not found");
	}
	
	@Test
	@DisplayName("findSupportTicketByCustomers() - Negative")
	void findSupportTicketByCustomersTest_Negative() {
		
		List <SupportTicket> supportTicketList = supportTicketRepository.findByCustomerID(2L);
		assertTrue(supportTicketList.isEmpty(),"SupportTicket list not found");
	}
	
	@Test
	@DisplayName("findSupportTicketByStatus() - Positive")
	void findSupportTicketByStatus_Positive() {
		SupportTicket ticket = SupportTicket.builder()
				.customerID(1L)
				.issueDescription("The ticket has been opened")
				.assignedAgent(1L)
				.status(Status.OPEN)
				.build();
		
		supportTicketRepository.save(ticket);
		
		List <SupportTicket> supportTicketList = supportTicketRepository.findByStatus(Status.OPEN);
		assertFalse(supportTicketList.isEmpty(),"SupportTicket list not found");
	}
	
	@Test
	@DisplayName("findSupportTicketByStatus() - Negative")
	void findSupportTicketByStatus_Negative() {
		
		List <SupportTicket> supportTicketList = supportTicketRepository.findByStatus(Status.CLOSED);
		assertTrue(supportTicketList.isEmpty(),"SupportTicket list not found");
	}
	@Test
	@DisplayName("saveSupportTicket() - Negative")
	void testSaveSupportTicket_negative() {
	    SupportTicket ticket = new SupportTicket();
	    ticket.setTicketID(1L);  // Conflict with @GeneratedValue
	    ticket.setCustomerID(1L);
	    ticket.setIssueDescription("The ticket has been closed");
	    ticket.setAssignedAgent(1L);
	    ticket.setStatus(Status.CLOSED);

	    Exception exception = assertThrows(Exception.class, () -> {
	        supportTicketRepository.save(ticket);
	    });

	    assertNotNull(exception, "An exception should be thrown when saving an invalid SupportTicket");
	}

}
