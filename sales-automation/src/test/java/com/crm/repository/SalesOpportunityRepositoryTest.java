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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class SalesOpportunityRepositoryTest {

    @Autowired
    private SalesOpportunityRepository repository;

    @Test
    @DisplayName("save() - Positive")
    public void testSaveSalesOpportunity_positive() {
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
    public void findSalesOpportunityByIdTest_Positive() {
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
    public void findSalesOpportunityByIdTest_Negative() {
        Optional<SalesOpportunity> savedOpportunity = repository.findById(999L); // Use an ID that doesn't exist
        assertFalse(savedOpportunity.isPresent(), "SalesOpportunity should not be found");
    }

    @Test
    @DisplayName("findAll() - Positive")
    public void getAllSalesOpportunities_Positive() {
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
    public void getAllSalesOpportunities_Negative() {
        List<SalesOpportunity> allOpportunities = repository.findAll();
        assertTrue(allOpportunities.isEmpty(), "SalesOpportunity list should be empty");
    }

    @Test
    @DisplayName("update() - Positive")
    public void updateSalesOpportunityTest_Positive(){
        SalesOpportunity opportunity = SalesOpportunity.builder()
                .customerID(1L)
                .estimatedValue(new BigDecimal("20000.0"))
                .salesStage(SalesStage.QUALIFICATION)
                .closingDate(LocalDate.now())
                .build();

        SalesOpportunity savedOpportunity = repository.save(opportunity);

        SalesOpportunity salesOpportunity = repository.findById(1L).get();
        salesOpportunity.setSalesStage(SalesStage.CLOSED_WON);
        SalesOpportunity newSalesOpportunity = repository.save(salesOpportunity);
        assertEquals(SalesStage.CLOSED_WON, newSalesOpportunity.getSalesStage());

    }

    @Test
    @DisplayName("delete() - Positive")
    public void deleteSalesOpportunityTest_Positive(){
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


}
