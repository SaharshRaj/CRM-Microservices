package com.crm.service;

import com.crm.dto.NotificationDTO;
import com.crm.dto.SalesOpportunityRequestDTO;
import com.crm.dto.SalesOpportunityResponseDTO;
import com.crm.dummy.DummyClass;
import com.crm.entities.EmailFormat;
import com.crm.entities.SalesOpportunity;
import com.crm.enums.SalesStage;
import com.crm.exception.CustomerNotFoundException;
import com.crm.exception.InvalidDateTimeException;
import com.crm.exception.InvalidOpportunityIdException;
import com.crm.exception.InvalidSalesDetailsException;
import com.crm.mapper.SalesOpportunityMapper;
import com.crm.repository.SalesOpportunityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j //TODO:Remove this annotation
public class SalesOpportunityServiceImpl implements SalesOpportunityService {

    private final SalesOpportunityRepository repository;
    private final DummyClass dummyClass;
    private String errorMsg = "No leads found with given Opportunity ID";

    @Autowired
    public SalesOpportunityServiceImpl(SalesOpportunityRepository repository, DummyClass dummyClass) {
        this.repository = repository;
        this.dummyClass = dummyClass;
    }


    /**
     * Retrieves all available leads.
     *
     * @return a list of SalesOpportunityDTO objects representing all sales opportunities.
     * @throws NoSuchElementException if no sales opportunities are found.
     */
    @Override
    public List<SalesOpportunityResponseDTO> retrieveAllSalesOpportunities() throws NoSuchElementException {

        List<SalesOpportunity> opportunityList = repository.findAll();

        List<SalesOpportunityResponseDTO> salesOpportunityResponseDTOList = new ArrayList<>();

        opportunityList.forEach(
                e -> salesOpportunityResponseDTOList.add(SalesOpportunityMapper.MAPPER.mapToResponseDTO(e))
        );

        if (salesOpportunityResponseDTOList.isEmpty()) {
            throw new NoSuchElementException("No Sales Opportunity Available");
        }

        return salesOpportunityResponseDTOList;
    }

    /**
     * Creates a new lead.
     *
     * @param salesOpportunityRequestDto the DTO representing the lead to be created.
     * @return the created SalesOpportunityDTO object.
     * @throws InvalidSalesDetailsException if there is an error during creation.
     */
    @Override
    public SalesOpportunityResponseDTO createSalesOpportunity(SalesOpportunityRequestDTO salesOpportunityRequestDto) throws InvalidSalesDetailsException {
        //TODO:Make Call Using Feign to check if Customer exists or not
        if(false){
            throw new CustomerNotFoundException("Customer with id: " + salesOpportunityRequestDto.getCustomerID() + " does not exists.");
        }
        SalesOpportunity salesOpportunity = SalesOpportunityMapper.MAPPER.mapToSalesOpportunity(salesOpportunityRequestDto);
        try {
            SalesOpportunity savedOpportunity = repository.save(salesOpportunity);
            return SalesOpportunityMapper.MAPPER.mapToResponseDTO(savedOpportunity);
        } catch (Exception e) {
            throw new InvalidSalesDetailsException(e.getMessage());
        }

    }

    /**
     * Update existing sales opportunity (deal).
     *
     * @param opportunityID ID of the lead to be updated.
     * @param salesOpportunityRequestDto The DTO representing the sales opportunity to be created.
     * @return The updated SalesOpportunityResponseDTO object.
     * @throws InvalidSalesDetailsException if there is an error during update.
     * @throws NoSuchElementException if no sales opportunity is found with the given ID.
     */
    @Override
    public SalesOpportunityResponseDTO updateSalesOpportunity(Long opportunityID ,SalesOpportunityRequestDTO salesOpportunityRequestDto) throws InvalidSalesDetailsException, NoSuchElementException{
        try{
        repository.findById(opportunityID).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(errorMsg);
        }
        SalesOpportunity newSalesOpportunity = SalesOpportunityMapper.MAPPER.mapToSalesOpportunity(salesOpportunityRequestDto);
        return SalesOpportunityMapper.MAPPER.mapToResponseDTO(repository.save(newSalesOpportunity));
    }

    /**
     * Update sales stage of existing opportunity (deal).
     *
     * @param opportunityID ID of the lead to be updated.
     * @param salesStage New Sales Stage
     * @return The updated SalesOpportunityResponseDTO object.
     * @throws IllegalArgumentException if wrong enum is provided.
     * @throws NoSuchElementException if no sales opportunity is found with the given ID.
     */
    public SalesOpportunityResponseDTO updateSalesStage(Long opportunityID ,SalesStage salesStage) throws InvalidSalesDetailsException, NoSuchElementException{
        try{
        SalesOpportunity salesOpportunity = repository.findById(opportunityID).orElseThrow();

        salesOpportunity.setSalesStage(salesStage);
        if(salesStage == SalesStage.CLOSED_LOST || salesStage == SalesStage.CLOSED_WON){
            salesOpportunity.setClosingDate(LocalDate.now());
        }
        EmailFormat email = EmailFormat.builder()
                .salutation("Dear employee,")
                .openingLine("I hope this message finds you well.")
                .body("This is to inform you that Sales Stage for Lead #" + opportunityID + " is changed to " + salesStage.name() + " at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + ".")
                .conclusion("THIS IS AN AUTO-GENERATED EMAIL. PLEASE DO NOT REPLY ON THIS!")
                .closing("SALES-AUTOMATION SERVICE \n CRM")
                .build();

        NotificationDTO notificationDTO = NotificationDTO.builder()
                .subject("Sales Status Changed for Lead with ID " + opportunityID)
                .body(email.toString())
                .build();
        //TODO:Send Email Regarding SalesStage
        NotificationDTO resultDTO = dummyClass.sendNotificatonDummy(notificationDTO);
        if (resultDTO.getStatus().equals("SENT")) {
            log.info("NOTIFICATION SENT FOR LEAD WITH ID #{}", opportunityID);
        } else {
            log.error("FAILED TO SEND NOTIFICATION FOR LEAD WITH ID #{}", opportunityID);
        }

        log.info("EMAIL -> \n{}",email.toString());
        return SalesOpportunityMapper.MAPPER.mapToResponseDTO(repository.save(salesOpportunity));
        }
        catch (NoSuchElementException e) {
            throw new NoSuchElementException(errorMsg);
        }
    }

    /**
     * Retrieves a lead by its opportunity ID.
     *
     * @param opportunityId the ID of the lead to be retrieved.
     * @return the SalesOpportunityDTO object representing the retrieved lead.
     * @throws NoSuchElementException if no opportunity is found with the given ID.
     */
    @Override
    public SalesOpportunityResponseDTO getOpportunitiesByOpportunity(Long opportunityId) throws NoSuchElementException {
        Optional<SalesOpportunity> salesOpportunity = repository.findById(opportunityId);
        if (salesOpportunity.isPresent()) {
            return SalesOpportunityMapper.MAPPER.mapToResponseDTO(salesOpportunity.get());
        } else {
            throw new NoSuchElementException(errorMsg);
        }
    }

    /**
     * Retrieves leads by customer ID.
     *
     * @param customerId the ID of the customer whose leads are to be retrieved.
     * @return a list of SalesOpportunityDTO objects representing the retrieved leads.
     * @throws NoSuchElementException if no opportunities are found for the given customer ID.
     */
    @Override
    public List<SalesOpportunityResponseDTO> getOpportunitiesByCustomer(Long customerId) throws NoSuchElementException {
        List<SalesOpportunity> salesOpportunityList = repository.findByCustomerID(customerId);
        if (salesOpportunityList.isEmpty()) {
            throw new NoSuchElementException("No leads found with given Customer ID");
        } else {
            List<SalesOpportunityResponseDTO> salesOpportunityRequestDTOList = new ArrayList<>();
            salesOpportunityList.forEach(
                    e -> salesOpportunityRequestDTOList.add(SalesOpportunityMapper.MAPPER.mapToResponseDTO(e)));
            return salesOpportunityRequestDTOList;
        }
    }

    /**
     * Retrieves leads by sales stage.
     *
     * @param salesStage the sales stage to filter leads by.
     * @return a list of SalesOpportunityDTO objects representing the retrieved leads.
     * @throws NoSuchElementException if no opportunities are found for the given sales stage.
     */
    @Override
    public List<SalesOpportunityResponseDTO> getOpportunitiesBySalesStage(SalesStage salesStage) throws NoSuchElementException {
        List<SalesOpportunity> salesOpportunityList = repository.findBySalesStage(salesStage);
        if (salesOpportunityList.isEmpty()) {
            throw new NoSuchElementException("No leads found with requested Sales Stage");
        } else {
            List<SalesOpportunityResponseDTO> salesOpportunityRequestDTOList = new ArrayList<>();
            salesOpportunityList.forEach(
                    e -> salesOpportunityRequestDTOList.add(SalesOpportunityMapper.MAPPER.mapToResponseDTO(e)));
            return salesOpportunityRequestDTOList;
        }
    }

    /**
     * Retrieves leads by estimated value.
     *
     * @param estimatedValue the estimated value to filter leads by.
     * @return a list of SalesOpportunityDTO objects representing the retrieved leads.
     * @throws NoSuchElementException if no opportunities are found for the given estimated value.
     */
    @Override
    public List<SalesOpportunityResponseDTO> getOpportunitiesByEstimatedValue(BigDecimal estimatedValue) throws NoSuchElementException {
        List<SalesOpportunity> salesOpportunityList = repository.findByEstimatedValue(estimatedValue);
        if (salesOpportunityList.isEmpty()) {
            throw new NoSuchElementException("No leads found with given Estimated Value");
        } else {
            List<SalesOpportunityResponseDTO> salesOpportunityDTOList = new ArrayList<>();
            salesOpportunityList.forEach(
                    e -> salesOpportunityDTOList.add(SalesOpportunityMapper.MAPPER.mapToResponseDTO(e)));
            return salesOpportunityDTOList;
        }
    }


    /**
     * Retrieves sales leads by closing date.
     *
     * @param closingDate the closing date to filter leads by.
     * @return a list of SalesOpportunityDTO objects representing the retrieved leads.
     * @throws NoSuchElementException if no opportunities are found for the given closing date.
     */
    @Override
    public List<SalesOpportunityResponseDTO> getOpportunitiesByClosingDate(LocalDate closingDate) throws NoSuchElementException {
        List<SalesOpportunity> salesOpportunityList = repository.findByClosingDate(closingDate);
        if (salesOpportunityList.isEmpty()) {
            throw new NoSuchElementException("No leads found with given Closing Date");
        } else {
            List<SalesOpportunityResponseDTO> salesOpportunityDTOList = new ArrayList<>();
            salesOpportunityList.forEach(
                    e -> salesOpportunityDTOList.add(SalesOpportunityMapper.MAPPER.mapToResponseDTO(e)));
            return salesOpportunityDTOList;
        }
    }

    /**
     * Retrieves leads by follow-up reminder date.
     *
     * @param followUpReminder the follow-up reminder date to filter leads by.
     * @return a list of SalesOpportunityDTO objects representing the retrieved leads.
     * @throws NoSuchElementException if no opportunities are found for the given follow-up reminder date.
     */
    @Override
    public List<SalesOpportunityResponseDTO> getOpportunitiesByFollowUpReminder(LocalDate followUpReminder) throws NoSuchElementException {
        List<SalesOpportunity> salesOpportunityList = repository.findByFollowUpReminder(followUpReminder);
        if (salesOpportunityList.isEmpty()) {
            throw new NoSuchElementException("No leads found with given Follow-up Reminder");
        } else {
            List<SalesOpportunityResponseDTO> salesOpportunityDTOList = new ArrayList<>();
            salesOpportunityList.forEach(
                    e -> salesOpportunityDTOList.add(SalesOpportunityMapper.MAPPER.mapToResponseDTO(e)));
            return salesOpportunityDTOList;
        }
    }

    /**
     * Schedules a follow-up reminder for a lead.
     *
     * @param opportunityId the ID of the opportunity to schedule the reminder for.
     * @param reminderDate  the date and time for the follow-up reminder.
     * @return the updated SalesOpportunityDTO object.
     * @throws InvalidDateTimeException      if the reminder date is in the past.
     * @throws InvalidOpportunityIdException if the opportunity ID is invalid.
     */
    @Override
    public SalesOpportunityResponseDTO scheduleFollowUpReminder(Long opportunityId, LocalDate reminderDate) throws InvalidDateTimeException, InvalidOpportunityIdException {
        if (reminderDate.isAfter(LocalDate.now())) {
            Optional<SalesOpportunity> optionalSalesOpportunity = repository.findById(opportunityId);
            if (optionalSalesOpportunity.isPresent()) {
                SalesOpportunity salesOpportunity = optionalSalesOpportunity.get();
                salesOpportunity.setFollowUpReminder(reminderDate);
                SalesOpportunity savedOpportunity = repository.save(salesOpportunity);
                return SalesOpportunityMapper.MAPPER.mapToResponseDTO(savedOpportunity);
            } else {
                throw new InvalidOpportunityIdException("Lead with Opportunity ID " + opportunityId + " does not exist.");
            }
        } else {
            throw new InvalidDateTimeException("Please enter valid date");
        }
    }

    /**
     * Deletes a sales lead by its opportunity ID.
     *
     * @param opportunityId the ID of the opportunity to be deleted.
     * @return true if the deletion was successful, false otherwise.
     * @throws InvalidOpportunityIdException if the opportunity ID is invalid.
     */
    @Override
    public boolean deleteByOpportunityID(Long opportunityId) throws InvalidOpportunityIdException {
        Optional<SalesOpportunity> salesOpportunity = repository.findById(opportunityId);
        if (salesOpportunity.isPresent()) {
            repository.delete(salesOpportunity.get());
            return true;
        } else {
            throw new InvalidOpportunityIdException("Lead with Opportunity ID " + opportunityId + " does not exist.");

        }
    }
}
