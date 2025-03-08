package com.crm.repository;

import com.crm.entities.SalesOpportunity;
import com.crm.enums.SalesStage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class SalesOpportunityRepositoryTest {

    @Autowired
    private SalesOpportunityRepository repository;

    @Test
    @DisplayName("save() - Positive")
    void testSaveSalesOpportunity_positive() {
        SalesOpportunity opportunity = SalesOpportunity.builder()
                .customerID(1L)
                .estimatedValue(new BigDecimal("10000.0"))
                .salesStage(SalesStage.PROSPECTING)
                .closingDate(LocalDate.now())
                .build();

        SalesOpportunity savedOpportunity = repository.save(opportunity);
        assertTrue(savedOpportunity.getOpportunityID() > 0, "Opportunity ID should be greater than 0");
    }

    @Test
    @DisplayName("findById() - Positive")
    void findSalesOpportunityByIdTest_Positive() {
        SalesOpportunity opportunity = SalesOpportunity.builder()
                .customerID(1L)
                .estimatedValue(new BigDecimal("20000.0"))
                .salesStage(SalesStage.QUALIFICATION)
                .closingDate(LocalDate.now())
                .build();

        SalesOpportunity savedOpportunity = repository.save(opportunity);
        Optional<SalesOpportunity> foundOpportunity = repository.findById(savedOpportunity.getOpportunityID());
        assertTrue(foundOpportunity.isPresent(), "SalesOpportunity not found");
        assertEquals(savedOpportunity.getOpportunityID(), foundOpportunity.get().getOpportunityID());
    }

    @Test
    @DisplayName("findById() - Negative")
    void findSalesOpportunityByIdTest_Negative() {
        Optional<SalesOpportunity> savedOpportunity = repository.findById(999L); // Use an ID that doesn't exist
        assertFalse(savedOpportunity.isPresent(), "SalesOpportunity should not be found");
    }

    @Test
    @DisplayName("findAll() - Positive")
    void getAllSalesOpportunities_Positive() {
        SalesOpportunity opportunity1 = SalesOpportunity.builder()
                .customerID(1L)
                .estimatedValue(new BigDecimal("30000.0"))
                .salesStage(SalesStage.CLOSED_WON)
                .closingDate(LocalDate.now())
                .build();

        SalesOpportunity opportunity2 = SalesOpportunity.builder()
                .customerID(2L)
                .estimatedValue(new BigDecimal("40000.0"))
                .salesStage(SalesStage.CLOSED_LOST)
                .closingDate(LocalDate.now())
                .build();

        repository.save(opportunity1);
        repository.save(opportunity2);

        List<SalesOpportunity> allOpportunities = repository.findAll();
        assertFalse(allOpportunities.isEmpty(), "SalesOpportunity list should not be empty");
        assertEquals(2, allOpportunities.size(), "SalesOpportunity list should contain 2 entries");
    }
    @Test
    @DisplayName("findAll() - Negative")
    void getAllSalesOpportunities_Negative() {
        List<SalesOpportunity> allOpportunities = repository.findAll();
        assertTrue(allOpportunities.isEmpty(), "SalesOpportunity list should be empty");
    }

    @Test
    @DisplayName("update() - Positive")
    void updateSalesOpportunityTest_Positive(){
        SalesOpportunity opportunity = SalesOpportunity.builder()
                .customerID(1L)
                .estimatedValue(new BigDecimal("20000.0"))
                .salesStage(SalesStage.QUALIFICATION)
                .closingDate(LocalDate.now())
                .build();

        repository.save(opportunity);

        SalesOpportunity salesOpportunity = repository.findById(1L).get();
        salesOpportunity.setSalesStage(SalesStage.CLOSED_WON);
        SalesOpportunity newSalesOpportunity = repository.save(salesOpportunity);
        assertEquals(SalesStage.CLOSED_WON, newSalesOpportunity.getSalesStage());

    }

    @Test
    @DisplayName("delete() - Positive")
    void deleteSalesOpportunityTest_Positive(){
        SalesOpportunity opportunity = SalesOpportunity.builder()
                .customerID(1L)
                .estimatedValue(new BigDecimal("20000.0"))
                .salesStage(SalesStage.QUALIFICATION)
                .closingDate(LocalDate.now())
                .build();

        SalesOpportunity savedOpportunity = repository.save(opportunity);

        repository.delete(savedOpportunity);
        Optional<SalesOpportunity> newSalesOpportunity = repository.findById(savedOpportunity.getOpportunityID());
        assertFalse(newSalesOpportunity.isPresent());

    }

    @Test
    @DisplayName("findByCustomerID() - Positive")
    void findSalesOpportunityByCustomersTest_Positive() {
        SalesOpportunity opportunity = SalesOpportunity.builder()
                .customerID(1L)
                .estimatedValue(new BigDecimal("20000.0"))
                .salesStage(SalesStage.QUALIFICATION)
                .closingDate(LocalDate.now())
                .build();

        repository.save(opportunity);
        List<SalesOpportunity> salesOpportunityList = repository.findByCustomerID(1L);
        assertFalse(salesOpportunityList.isEmpty(), "SalesOpportunity not found");
    }

    @Test
    @DisplayName("findByCustomerID() - Negative")
    void findSalesOpportunityByCustomersTest_Negative() {

        List<SalesOpportunity> salesOpportunityList = repository.findByCustomerID(2L);
        assertTrue(salesOpportunityList.isEmpty(), "SalesOpportunity not found");
    }

    @Test
    @DisplayName("findBySalesStage() - Positive")
    void findSalesOpportunityBySalesStage_Positive() {
        SalesOpportunity opportunity = SalesOpportunity.builder()
                .customerID(1L)
                .estimatedValue(new BigDecimal("20000.0"))
                .salesStage(SalesStage.QUALIFICATION)
                .closingDate(LocalDate.now())
                .build();

        repository.save(opportunity);
        List<SalesOpportunity> salesOpportunityList = repository.findBySalesStage(SalesStage.QUALIFICATION);
        assertFalse(salesOpportunityList.isEmpty(), "SalesOpportunity not found");
    }

    @Test
    @DisplayName("findBySalesStage() - Negative")
    void findSalesOpportunityBySalesStage_Negative() {

        List<SalesOpportunity> salesOpportunityList = repository.findBySalesStage(SalesStage.PROSPECTING);
        assertTrue(salesOpportunityList.isEmpty(), "SalesOpportunity not found");
    }

    @Test
    @DisplayName("findByEstimatedValue() - Positive")
    void findSalesOpportunityByEstimatedValue_Positive() {
        SalesOpportunity opportunity = SalesOpportunity.builder()
                .customerID(1L)
                .estimatedValue(new BigDecimal("20000.0"))
                .salesStage(SalesStage.QUALIFICATION)
                .closingDate(LocalDate.now())
                .build();

        repository.save(opportunity);
        List<SalesOpportunity> salesOpportunityList = repository.findByEstimatedValue(BigDecimal.valueOf(20000.0));
        assertFalse(salesOpportunityList.isEmpty(), "SalesOpportunity not found");
    }

    @Test
    @DisplayName("findByEstimatedValue() - Negative")
    void findSalesOpportunityByEstimatedValue_Negative() {

        List<SalesOpportunity> salesOpportunityList = repository.findByEstimatedValue(BigDecimal.valueOf(100000.0));
        assertTrue(salesOpportunityList.isEmpty(), "SalesOpportunity not found");
    }

    @Test
    @DisplayName("findByClosingDate() - Positive")
    void findSalesOpportunityByClosingDate_Positive() {
        SalesOpportunity opportunity = SalesOpportunity.builder()
                .customerID(1L)
                .estimatedValue(new BigDecimal("20000.0"))
                .salesStage(SalesStage.QUALIFICATION)
                .closingDate(LocalDate.now())
                .build();

        SalesOpportunity save = repository.save(opportunity);
        List<SalesOpportunity> salesOpportunityList = repository.findByClosingDate(save.getClosingDate());
        assertFalse(salesOpportunityList.isEmpty(), "SalesOpportunity not found");
    }

    @Test
    @DisplayName("findByClosingDate() - Negative")
    void findSalesOpportunityByClosingDate_Negative() {

        List<SalesOpportunity> salesOpportunityList = repository.findByClosingDate(LocalDate.now());
        assertTrue(salesOpportunityList.isEmpty(), "SalesOpportunity not found");
    }


    @Test
    @DisplayName("findByFollowUpReminder() - Positive")
    void findSalesOpportunityByFollowUpReminder_Positive() {
        SalesOpportunity opportunity = SalesOpportunity.builder()
                .customerID(1L)
                .estimatedValue(new BigDecimal("20000.0"))
                .salesStage(SalesStage.QUALIFICATION)
                .closingDate(LocalDate.now())
                .followUpReminder(LocalDate.of(2020, Month.JANUARY, 18).atStartOfDay())
                .build();

        repository.save(opportunity);
        List<SalesOpportunity> salesOpportunityList = repository.findByFollowUpReminder(LocalDate.of(2020, Month.JANUARY, 18).atStartOfDay());
        assertFalse(salesOpportunityList.isEmpty(), "SalesOpportunity not found");
    }

    @Test
    @DisplayName("findByFollowUpReminder() - Negative")
    void findSalesOpportunityByFollowUpReminder_Negative() {

        List<SalesOpportunity> salesOpportunityList = repository.findByFollowUpReminder(LocalDate.of(2020, Month.JANUARY, 18).atStartOfDay());
        assertTrue(salesOpportunityList.isEmpty(), "SalesOpportunity not found");
    }

}
