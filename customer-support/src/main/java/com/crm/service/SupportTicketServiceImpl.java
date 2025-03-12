package com.crm.service;

import com.crm.dto.SupportTicketDTO;
import com.crm.entities.SupportTicket;
import com.crm.mapper.SupportTicketMapper;
import com.crm.repository.SupportTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SupportTicketServiceImpl implements SupportTicketService {
    
    @Autowired
    private SupportTicketRepository supportTicketRepository;
    
public List<SupportTicketDTO> retrieveAllProfiles(){
	
	 // Retrieve all profiles from repository
  List<SupportTicket> allProfiles = supportTicketRepository.findAll();

  // Declare DTO list
  List<SupportTicketDTO> resultList = new ArrayList<>();

  // Populate List
  allProfiles.forEach(e -> {
      // Use MAPPER method to convert entity to DTO
      SupportTicketDTO supportTicketDTO = SupportTicketMapper.MAPPER.mapToDTO(e);
      // Add DTO to list
      resultList.add(supportTicketDTO);
  });
  //Return Result
  return resultList;
}
    
    /**
     * Add Method Info for documentation
   */

	@Override
	public SupportTicket createTicket(SupportTicket supportTicket) {
		return supportTicketRepository.save(supportTicket);
	}

	@Override
	public SupportTicket updateTicket(Long ticketId, SupportTicket ticket) {
		Optional<SupportTicket> existingTicket = supportTicketRepository.findById(ticketId);
        if (existingTicket.isPresent()) {
            SupportTicket updatedTicket = existingTicket.get();
            updatedTicket.setIssueDescription(ticket.getIssueDescription());
            updatedTicket.setAssignedAgent(ticket.getAssignedAgent());
            updatedTicket.setStatus(ticket.getStatus());
            return supportTicketRepository.save(updatedTicket);
        } else {
            throw new RuntimeException("Ticket not found with id " + ticketId);
        }
	}

	@Override
	public SupportTicket getTicketById(Long ticketId) {
		return supportTicketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id " + ticketId));
	}

	@Override
	public List<SupportTicket> getAllTickets() {
		return supportTicketRepository.findAll();
	}

	@Override
	public void deleteTicket(Long ticketId) {
		Optional<SupportTicket> supportTicket = supportTicketRepository.findById(ticketId);
		if(supportTicket.isPresent()){
			supportTicketRepository.delete(supportTicket.get());
		}
		else{
			throw new RuntimeException("OBJECT NOT PRESENT");
		}

	}

}
