package com.crm.service;

import com.crm.dto.SalesOpportunityDTO;
import com.crm.entities.SalesOpportunity;
import com.crm.enums.SalesStage;
import com.crm.exception.InvalidCustomerIdException;
import com.crm.exception.InvalidDateTimeException;
import com.crm.exception.InvalidSalesDetailsException;
import com.crm.mapper.SalesOppurtunityMapper;
import com.crm.repository.SalesOpportunityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalesOpportunityServiceImplTest {


    @Mock
    private SalesOpportunityRepository repository;

    @Mock
    private SalesOppurtunityMapper mapper;

    @InjectMocks
    private SalesOpportunityService service;

    private static List<SalesOpportunity> list;

    @BeforeEach
    void setUp() {
        list = new ArrayList<>(List.of(
                SalesOpportunity.builder()
                        .opportunityID(1L)
                        .customerID(1L)
                        .estimatedValue(new BigDecimal("10000.0"))
                        .salesStage(SalesStage.PROSPECTING)
                        .closingDate(LocalDate.of(2020, Month.JANUARY, 18))
                        .followUpReminder(LocalDate.of(2025, Month.APRIL, 18).atStartOfDay())
                        .build(),
                SalesOpportunity.builder()
                        .opportunityID(2L)
                        .customerID(2L)
                        .estimatedValue(new BigDecimal("10000.0"))
                        .salesStage(SalesStage.PROSPECTING)
                        .closingDate(LocalDate.of(2020, Month.JANUARY, 18))
                        .followUpReminder(LocalDate.of(2025, Month.APRIL, 18).atStartOfDay())
                        .build(),
                SalesOpportunity.builder()
                        .opportunityID(3L)
                        .customerID(3L)
                        .estimatedValue(new BigDecimal("10000.0"))
                        .salesStage(SalesStage.PROSPECTING)
                        .closingDate(LocalDate.of(2020, Month.JANUARY, 18))
                        .followUpReminder(LocalDate.of(2025, Month.APRIL, 18).atStartOfDay())
                        .build()
        ));
    }

    @Test
    @DisplayName("retrieveAllSalesOpportunities() - Positive")
    void testRetrieveAllSalesOpportunities_Positive() {
        when(repository.findAll()).thenReturn(list);
        assertFalse(service.retrieveAllSalesOpportunities().isEmpty());
    }

    @Test
    @DisplayName("retrieveAllSalesOpportunities() - Negative")
    void testRetrieveAllSalesOpportunities_Negative() {
        when(repository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(service.retrieveAllSalesOpportunities().isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("createSalesOpportunity() - Positive")
    void testCreateSalesOpportunity_Positive() {
        SalesOpportunity salesOpportunity = list.getFirst();

        when(repository.save(salesOpportunity)).thenAnswer((invocation -> {
            salesOpportunity.setOpportunityID(1L);
            return  salesOpportunity;
        }));
        when(mapper.mapToDTO(salesOpportunity)).thenAnswer(invocation -> {

            return SalesOpportunityDTO
                    .builder()
                    .opportunityID(salesOpportunity.getOpportunityID())
                    .salesStage(salesOpportunity.getSalesStage())
                    .estimatedValue(salesOpportunity.getEstimatedValue())
                    .followUpReminder(salesOpportunity.getFollowUpReminder())
                    .closingDate(salesOpportunity.getClosingDate())
                    .customerID(salesOpportunity.getCustomerID())
                    .build();
        });

        assertEquals(1L,
                service.createSalesOpportunity(mapper
                .mapToDTO(salesOpportunity))
                .getOpportunityID()
        );
        verify(repository, times(1)).save(salesOpportunity);
    }

    @Test
    @DisplayName("createSalesOpportunity() - Negative")
    void testCreateSalesOpportunity_Negative() {

        SalesOpportunityDTO salesOpportunityDTO = new SalesOpportunityDTO();

        when(mapper.mapToSalesOpportunity(salesOpportunityDTO)).thenAnswer(invocation -> {

            return SalesOpportunity
                    .builder()
                    .opportunityID(salesOpportunityDTO.getOpportunityID())
                    .salesStage(salesOpportunityDTO.getSalesStage())
                    .estimatedValue(salesOpportunityDTO.getEstimatedValue())
                    .followUpReminder(salesOpportunityDTO.getFollowUpReminder())
                    .closingDate(salesOpportunityDTO.getClosingDate())
                    .customerID(salesOpportunityDTO.getCustomerID())
                    .build();
        });

        assertThrows(InvalidSalesDetailsException.class, () -> service.createSalesOpportunity(salesOpportunityDTO));
        verify(repository, times(1)).save(mapper.mapToSalesOpportunity(salesOpportunityDTO));
    }

    @Test
    @DisplayName("getOpportunitiesByCustomer() - Positive")
    void testGetOpportunitiesByCustomer_Positive() {
        when(repository.findById(1L)).thenAnswer((invocation -> {
            SalesOpportunity salesOpportunity = null;
            for(SalesOpportunity s : list){
                if(s.getOpportunityID() == 1L){
                    salesOpportunity = s;
                }
            }
            return  salesOpportunity;
        }));

        List<SalesOpportunityDTO> opportunitiesByCustomer = service.getOpportunitiesByCustomer(1L);

        assertFalse(opportunitiesByCustomer.isEmpty());
    }

    @Test
    @DisplayName("getOpportunitiesByCustomer() - Negative")
    void testGetOpportunitiesByCustomer_Negative() {
    when(repository.findById(1L)).thenReturn(Optional.ofNullable(new  SalesOpportunity()));
    assertThrows(InvalidCustomerIdException.class, () -> service.getOpportunitiesByCustomer(1L));
    verify(repository, times(0)).findById(anyLong());
    }

    @Test
    @DisplayName("getOpportunitiesBySalesStage() - Positive")
    void testGetOpportunitiesBySalesStage_Positive() {
        when(repository.findBySalesStage(SalesStage.PROSPECTING)).thenAnswer((invocation -> {
            List<SalesOpportunity> opportunityList = new ArrayList<>();
            list.forEach(e -> {
                if(e.getSalesStage() == SalesStage.PROSPECTING){
                    opportunityList.add(e);
                }
            });
            return  opportunityList;
        }));

        List<SalesOpportunityDTO> opportunitiesBySalesStage = service.getOpportunitiesBySalesStage(SalesStage.PROSPECTING);

        assertFalse(opportunitiesBySalesStage.isEmpty());
    }

    @Test
    @DisplayName("getOpportunitiesBySalesStage() - Negative")
    void testGetOpportunitiesBySalesStage_Negative() {
        when(repository.findBySalesStage(SalesStage.PROSPECTING)).thenReturn(new ArrayList<>());

        List<SalesOpportunityDTO> opportunitiesBySalesStage = service.getOpportunitiesBySalesStage(SalesStage.PROSPECTING);

        assertTrue(opportunitiesBySalesStage.isEmpty());
    }

    @Test
    @DisplayName("getOpportunitiesByEstimatedValue() - Positive")
    void testGetOpportunitiesByEstimatedValue_Positive() {
        when(repository.findByEstimatedValue(new BigDecimal("10000.0"))).thenAnswer((invocation -> {
            List<SalesOpportunity> opportunityList = new ArrayList<>();
            list.forEach(e -> {
                if(e.getEstimatedValue() == new BigDecimal("10000.0")){
                    opportunityList.add(e);
                }
            });
            return  opportunityList;
        }));

        List<SalesOpportunityDTO> opportunitiesByEstimatedValue = service.getOpportunitiesByEstimatedValue(new BigDecimal("10000.0"));

        assertFalse(opportunitiesByEstimatedValue.isEmpty());
    }

    @Test
    @DisplayName("getOpportunitiesByEstimatedValue() - Negative")
    void testGetOpportunitiesByEstimatedValue_Negative() {
        when(repository.findByEstimatedValue(new BigDecimal("10000.0"))).thenReturn(new ArrayList<>());

        List<SalesOpportunityDTO> opportunitiesByEstimatedValue = service.getOpportunitiesByEstimatedValue(new BigDecimal("10000.0"));

        assertTrue(opportunitiesByEstimatedValue.isEmpty());
    }

    @Test
    @DisplayName("getOpportunitiesByClosingDate() - Positive")
    void testGetOpportunitiesByClosingDate_Positive() {
        when(repository.findByClosingDate(LocalDate.of(2020, Month.JANUARY, 18))).thenAnswer((invocation -> {
            List<SalesOpportunity> opportunityList = new ArrayList<>();
            list.forEach(e -> {
                if(e.getClosingDate() == LocalDate.of(2020, Month.JANUARY, 18)){
                    opportunityList.add(e);
                }
            });
            return  opportunityList;
        }));

        List<SalesOpportunityDTO> opportunitiesByClosingDate = service.getOpportunitiesByEstimatedValue(new BigDecimal("10000.0"));

        assertFalse(opportunitiesByClosingDate.isEmpty());
    }

    @Test
    @DisplayName("getOpportunitiesByClosingDate() - Negative")
    void testGetOpportunitiesByClosingDate_Negative() {
        when(repository.findByClosingDate(LocalDate.of(2020, Month.JANUARY, 18))).thenReturn(new ArrayList<>());

        List<SalesOpportunityDTO> opportunitiesByClosingDate = service.getOpportunitiesByEstimatedValue(new BigDecimal("10000.0"));

        assertTrue(opportunitiesByClosingDate.isEmpty());
    }

    @Test
    @DisplayName("getOpportunitiesByFollowUpReminder() - Positive")
    void testGetOpportunitiesByFollowUpReminder_Positive() {
        when(repository.findByFollowUpReminder(LocalDate.of(2025, Month.APRIL, 18).atStartOfDay())).thenAnswer((invocation -> {
            List<SalesOpportunity> opportunityList = new ArrayList<>();
            list.forEach(e -> {
                if(e.getFollowUpReminder() == LocalDate.of(2025, Month.APRIL, 18).atStartOfDay()){
                    opportunityList.add(e);
                }
            });
            return  opportunityList;
        }));

        List<SalesOpportunityDTO> opportunitiesByFollowUpReminder = service.getOpportunitiesByEstimatedValue(new BigDecimal("10000.0"));

        assertFalse(opportunitiesByFollowUpReminder.isEmpty());
    }

    @Test
    @DisplayName("getOpportunitiesByFollowUpReminder() - Negative")
    void testGetOpportunitiesByFollowUpReminder_Negative() {
        when(repository.findByFollowUpReminder(LocalDate.of(2025, Month.APRIL, 18).atStartOfDay())).thenReturn(new ArrayList<>());

        List<SalesOpportunityDTO> opportunitiesByFollowUpReminder = service.getOpportunitiesByEstimatedValue(new BigDecimal("10000.0"));

        assertTrue(opportunitiesByFollowUpReminder.isEmpty());
    }


    @Test
    @DisplayName("scheduleFollowUpReminder() - Positive")
    void testScheduleFollowUpReminder_Positive() {
        SalesOpportunity salesOpportunity = list.getFirst();
        LocalDateTime localDateTime = LocalDate.of(2025, Month.APRIL, 18).atStartOfDay();
        when(repository.findById(1L)).thenReturn(Optional.ofNullable(salesOpportunity));
        when(repository.save(salesOpportunity)).thenAnswer(invocation -> {
            salesOpportunity.setFollowUpReminder(localDateTime);
            return salesOpportunity;
        });
        assertEquals(localDateTime, service.scheduleFollowUpReminder(1L,localDateTime).getFollowUpReminder());
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("scheduleFollowUpReminder() - Negative")
    void testScheduleFollowUpReminder_Negative() {
        LocalDateTime localDateTime = LocalDate.of(2020, Month.JANUARY, 18).atStartOfDay();

        assertThrows(InvalidDateTimeException.class, () -> service.scheduleFollowUpReminder(1L,localDateTime));
        verify(repository, times(0)).findById(anyLong());
        verify(repository, times(0)).save(any());
    }




}