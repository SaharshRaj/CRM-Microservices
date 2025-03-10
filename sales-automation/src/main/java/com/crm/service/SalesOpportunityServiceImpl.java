package com.crm.service;

import com.crm.dto.SalesOpportunityDTO;
import com.crm.entities.SalesOpportunity;
import com.crm.enums.SalesStage;
import com.crm.exception.InvalidOpportunityIdException;
import com.crm.exception.InvalidDateTimeException;
import com.crm.exception.InvalidSalesDetailsException;
import com.crm.mapper.SalesOppurtunityMapper;
import com.crm.repository.SalesOpportunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SalesOpportunityServiceImpl implements SalesOpportunityService {

    @Autowired
    private SalesOpportunityRepository repository;


    /**
     * Retrieves all available leads.
     *
     * @return a list of SalesOpportunityDTO objects representing all sales opportunities.
     * @throws NoSuchElementException if no sales opportunities are found.
     */
    @Override
    public List<SalesOpportunityDTO> retrieveAllSalesOpportunities() throws NoSuchElementException {

        List<SalesOpportunity> opportunityList = repository.findAll();

        List<SalesOpportunityDTO> salesOpportunityDTOList = new ArrayList<>();

        opportunityList.forEach(
                e -> salesOpportunityDTOList.add(SalesOppurtunityMapper.MAPPER.mapToDTO(e))
        );

        if(salesOpportunityDTOList.isEmpty()){
            throw new NoSuchElementException("No Sales Opportunity Available");
        }

        return salesOpportunityDTOList;
    }

    /**
     * Creates a new lead.
     *
     * @param salesOpportunityDto the DTO representing the lead to be created.
     * @return the created SalesOpportunityDTO object.
     * @throws InvalidSalesDetailsException if there is an error during creation.
     */
    @Override
    public SalesOpportunityDTO createSalesOpportunity(SalesOpportunityDTO salesOpportunityDto) throws InvalidSalesDetailsException {
        SalesOpportunity salesOpportunity = SalesOppurtunityMapper.MAPPER.mapToSalesOpportunity(salesOpportunityDto);
        try {
            SalesOpportunity savedOpportunity = repository.save(salesOpportunity);
            return SalesOppurtunityMapper.MAPPER.mapToDTO(savedOpportunity);
        } catch (Exception e) {
            throw new InvalidSalesDetailsException(e.getMessage());
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
    public SalesOpportunityDTO getOpportunitiesByOpportunity(Long opportunityId) throws NoSuchElementException {
        Optional<SalesOpportunity> salesOpportunity = repository.findById(opportunityId);
        if(salesOpportunity.isPresent()){
            return  SalesOppurtunityMapper.MAPPER.mapToDTO(salesOpportunity.get());
        }
        else {
            throw new NoSuchElementException("No leads found with given Opportunity ID");
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
    public List<SalesOpportunityDTO> getOpportunitiesByCustomer(Long customerId) throws NoSuchElementException {
        List<SalesOpportunity> salesOpportunityList = repository.findByCustomerID(customerId);
        if(salesOpportunityList.isEmpty()){
            throw new NoSuchElementException("No leads found with given Customer ID");
        }
        else {
        List<SalesOpportunityDTO> salesOpportunityDTOList = new ArrayList<>();
        salesOpportunityList.forEach(
                e -> salesOpportunityDTOList.add(SalesOppurtunityMapper.MAPPER.mapToDTO(e)));
        return salesOpportunityDTOList;
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
    public List<SalesOpportunityDTO> getOpportunitiesBySalesStage(SalesStage salesStage) throws NoSuchElementException {
        List<SalesOpportunity> salesOpportunityList = repository.findBySalesStage(salesStage);
        if(salesOpportunityList.isEmpty()){
            throw new NoSuchElementException("No leads found with requested Sales Stage");
        }
        else {
            List<SalesOpportunityDTO> salesOpportunityDTOList = new ArrayList<>();
            salesOpportunityList.forEach(
                    e -> salesOpportunityDTOList.add(SalesOppurtunityMapper.MAPPER.mapToDTO(e)));
            return salesOpportunityDTOList;
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
    public List<SalesOpportunityDTO> getOpportunitiesByEstimatedValue(BigDecimal estimatedValue) throws NoSuchElementException {
        List<SalesOpportunity> salesOpportunityList = repository.findByEstimatedValue(estimatedValue);
        if(salesOpportunityList.isEmpty()){
            throw new NoSuchElementException("No leads found with given Estimated Value");
        }
        else {
            List<SalesOpportunityDTO> salesOpportunityDTOList = new ArrayList<>();
            salesOpportunityList.forEach(
                    e -> salesOpportunityDTOList.add(SalesOppurtunityMapper.MAPPER.mapToDTO(e)));
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
    public List<SalesOpportunityDTO> getOpportunitiesByClosingDate(LocalDate closingDate) throws NoSuchElementException {
        List<SalesOpportunity> salesOpportunityList = repository.findByClosingDate(closingDate);
        if(salesOpportunityList.isEmpty()){
            throw new NoSuchElementException("No leads found with given Closing Date");
        }
        else {
            List<SalesOpportunityDTO> salesOpportunityDTOList = new ArrayList<>();
            salesOpportunityList.forEach(
                    e -> salesOpportunityDTOList.add(SalesOppurtunityMapper.MAPPER.mapToDTO(e)));
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
    public List<SalesOpportunityDTO> getOpportunitiesByFollowUpReminder(LocalDateTime followUpReminder) throws NoSuchElementException {
        List<SalesOpportunity> salesOpportunityList = repository.findByFollowUpReminder(followUpReminder);
        if(salesOpportunityList.isEmpty()){
            throw new NoSuchElementException("No leads found with given Follow-up Reminder");
        }
        else {
            List<SalesOpportunityDTO> salesOpportunityDTOList = new ArrayList<>();
            salesOpportunityList.forEach(
                    e -> salesOpportunityDTOList.add(SalesOppurtunityMapper.MAPPER.mapToDTO(e)));
            return salesOpportunityDTOList;
        }
    }

    /**
     * Schedules a follow-up reminder for a lead.
     *
     * @param opportunityId the ID of the opportunity to schedule the reminder for.
     * @param reminderDate the date and time for the follow-up reminder.
     * @return the updated SalesOpportunityDTO object.
     * @throws InvalidDateTimeException if the reminder date is in the past.
     * @throws InvalidOpportunityIdException if the opportunity ID is invalid.
     */
    @Override
    public SalesOpportunityDTO scheduleFollowUpReminder(Long opportunityId, LocalDateTime reminderDate) throws InvalidDateTimeException, InvalidOpportunityIdException {
        if(reminderDate.isAfter(LocalDateTime.now())){
            Optional<SalesOpportunity> optionalSalesOpportunity = repository.findById(opportunityId);
            if(optionalSalesOpportunity.isPresent()){
                SalesOpportunity salesOpportunity = optionalSalesOpportunity.get();
                salesOpportunity.setFollowUpReminder(reminderDate);
                SalesOpportunity savedOpportunity  = repository.save(salesOpportunity);
                return SalesOppurtunityMapper.MAPPER.mapToDTO(savedOpportunity);
            }
            else{
                throw new InvalidOpportunityIdException("Lead with Opportunity ID "+opportunityId+" does not exist.");
            }
        }
        else{
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
        if(salesOpportunity.isPresent()){
            repository.delete(salesOpportunity.get());
            return true;
        }
        else{
            throw new InvalidOpportunityIdException("Lead with Opportunity ID "+opportunityId+" does not exist.");

        }
    }
}
