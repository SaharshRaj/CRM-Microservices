package com.crm.service;

import com.crm.dto.SalesOpportunityRequestDTO;
import com.crm.dto.SalesOpportunityResponseDTO;
import com.crm.entities.SalesOpportunity;
import com.crm.enums.SalesStage;
import com.crm.exception.InvalidDateTimeException;
import com.crm.exception.InvalidOpportunityIdException;
import com.crm.exception.InvalidSalesDetailsException;
import com.crm.mapper.SalesOpportunityMapper;
import com.crm.repository.SalesOpportunityRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SalesOpportunityServiceImplTest {


    @Mock
    private SalesOpportunityRepository repository;

    @Mock
    private SalesOpportunityMapper mapper;


    @InjectMocks
    private SalesOpportunityServiceImpl service;

    private static List<SalesOpportunity> list;

    @BeforeEach
    void setUp() {
        list = new ArrayList<>(List.of(
                SalesOpportunity.builder()
                        .opportunityID(1L)
                        .customerID(1L)
                        .estimatedValue(new BigDecimal("10000.0"))
                        .salesStage(SalesStage.PROSPECTING)
                        .closingDate(LocalDate.of(2025, Month.MAY, 18))
                        .followUpReminder(LocalDate.of(2025, Month.APRIL, 18))
                        .build(),
                SalesOpportunity.builder()
                        .opportunityID(2L)
                        .customerID(2L)
                        .estimatedValue(new BigDecimal("10000.0"))
                        .salesStage(SalesStage.PROSPECTING)
                        .closingDate(LocalDate.of(2025, Month.MAY, 18))
                        .followUpReminder(LocalDate.of(2025, Month.APRIL, 18))
                        .build(),
                SalesOpportunity.builder()
                        .opportunityID(3L)
                        .customerID(3L)
                        .estimatedValue(new BigDecimal("10000.0"))
                        .salesStage(SalesStage.PROSPECTING)
                        .closingDate(LocalDate.of(2025, Month.MAY, 18))
                        .followUpReminder(LocalDate.of(2025, Month.APRIL, 18))
                        .build()
        ));
    }

    @Test
    @DisplayName("retrieveAllSalesOpportunities() - Positive")
    void retrieveAllSalesOpportunitiesShouldReturnListOfOpportunitiesWhenDataExists() {
        when(repository.findAll()).thenReturn(list);
        assertFalse(service.retrieveAllSalesOpportunities().isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("retrieveAllSalesOpportunities() - Negative")
    void retrieveAllSalesOpportunitiesShouldThrowExceptionWhenNoDataExists() {
        when(repository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(NoSuchElementException.class, () -> service.retrieveAllSalesOpportunities());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("createSalesOpportunity() - Positive")
    void createSalesOpportunityShouldReturnCreatedOpportunityWhenValidInput() {
        SalesOpportunity salesOpportunity = list.getFirst();

        when(repository.save(any(SalesOpportunity.class))).thenAnswer((invocation -> {
            salesOpportunity.setOpportunityID(1L);
            return salesOpportunity;
        }));
        when(mapper.mapToRequestDTO(salesOpportunity)).thenAnswer(invocation -> SalesOpportunityRequestDTO
                .builder()
                .opportunityID(salesOpportunity.getOpportunityID())
                .salesStage(salesOpportunity.getSalesStage())
                .estimatedValue(salesOpportunity.getEstimatedValue())
                .followUpReminder(salesOpportunity.getFollowUpReminder())
                .closingDate(salesOpportunity.getClosingDate())
                .customerID(salesOpportunity.getCustomerID())
                .build());

        assertEquals(1L,
                service.createSalesOpportunity(mapper
                                .mapToRequestDTO(salesOpportunity))
                        .getOpportunityID()
        );
        verify(repository, times(1)).save(any(SalesOpportunity.class));
    }

    @Test
    @DisplayName("createSalesOpportunity() - Negative")
    void createSalesOpportunityShouldThrowExceptionWhenInvalidInput() {
        SalesOpportunity salesOpportunity = list.getFirst();

        salesOpportunity.setCustomerID(0L);

        when(repository.save(any(SalesOpportunity.class))).thenThrow(ConstraintViolationException.class);

        when(mapper.mapToRequestDTO(salesOpportunity)).thenAnswer(invocation -> SalesOpportunityRequestDTO
                .builder()
                .opportunityID(salesOpportunity.getOpportunityID())
                .salesStage(salesOpportunity.getSalesStage())
                .estimatedValue(salesOpportunity.getEstimatedValue())
                .followUpReminder(salesOpportunity.getFollowUpReminder())
                .closingDate(salesOpportunity.getClosingDate())
                .customerID(salesOpportunity.getCustomerID())
                .build());
        // Assert that an InvalidSalesDetailsException is thrown when creating the SalesOpportunity
        SalesOpportunityRequestDTO salesOpportunityRequestDTO = mapper.mapToRequestDTO(salesOpportunity);
        assertThrows(InvalidSalesDetailsException.class, () -> service.createSalesOpportunity(salesOpportunityRequestDTO));
        verify(repository, times(1)).save(any(SalesOpportunity.class));
    }

    @Test
    @DisplayName("getOpportunitiesByOpportunityID() - Positive")
    void getOpportunitiesByOpportunityIdShouldReturnOpportunityWhenIdExists() {
        when(repository.findById(1L)).thenAnswer((invocation -> {
            for (SalesOpportunity s : list) {
                if (s.getOpportunityID() == 1L) {
                    return Optional.of(s);
                }
            }
            return Optional.empty();
        }));

        SalesOpportunityResponseDTO salesOpportunityResponseDTO = service.getOpportunitiesByOpportunity(1L);

        assertEquals(1L, (long) salesOpportunityResponseDTO.getOpportunityID());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getOpportunitiesByOpportunityID() - Negative")
    void getOpportunitiesByOpportunityIdShouldThrowExceptionWhenIdDoesNotExist() {
        when(repository.findById(0L)).thenAnswer((invocation -> {
            for (SalesOpportunity s : list) {
                if (s.getOpportunityID() == 0L) {
                    return Optional.of(s);
                }
            }
            return Optional.empty();
        }));

        assertThrows(NoSuchElementException.class, () -> service.getOpportunitiesByOpportunity(0L));
        verify(repository, times(1)).findById(anyLong());
    }


    @Test
    @DisplayName("getOpportunitiesByCustomer() - Positive")
    void getOpportunitiesByCustomerIdShouldReturnListOfOpportunitiesWhenCustomerExists() {
        when(repository.findByCustomerID(1L)).thenAnswer((invocation -> {
            List<SalesOpportunity> salesOpportunityList = new ArrayList<>();
            for (SalesOpportunity s : list) {
                if (s.getCustomerID() == 1L) {
                    salesOpportunityList.add(s);
                }
            }
            return salesOpportunityList;
        }));

        List<SalesOpportunityResponseDTO> opportunitiesByCustomer = service.getOpportunitiesByCustomer(1L);

        assertFalse(opportunitiesByCustomer.isEmpty());
        verify(repository, times(1)).findByCustomerID(1L);
    }

    @Test
    @DisplayName("getOpportunitiesByCustomer() - Negative")
    void getOpportunitiesByCustomerIdShouldThrowExceptionWhenCustomerDoesNotExist() {
        when(repository.findByCustomerID(0L)).thenAnswer((invocation -> {
            List<SalesOpportunity> salesOpportunityList = new ArrayList<>();
            for (SalesOpportunity s : list) {
                if (s.getCustomerID() == 0L) {
                    salesOpportunityList.add(s);
                }
            }
            return salesOpportunityList;
        }));
        assertThrows(NoSuchElementException.class, () -> service.getOpportunitiesByCustomer(0L));
        verify(repository, times(1)).findByCustomerID(anyLong());
    }

    @Test
    @DisplayName("getOpportunitiesBySalesStage() - Positive")
    void getOpportunitiesBySalesStageShouldReturnListOfOpportunitiesWhenSalesStageExists() {
        when(repository.findBySalesStage(SalesStage.PROSPECTING)).thenAnswer((invocation -> {
            List<SalesOpportunity> opportunityList = new ArrayList<>();
            list.forEach(e -> {
                if (e.getSalesStage() == SalesStage.PROSPECTING) {
                    opportunityList.add(e);
                }
            });
            return opportunityList;
        }));

        List<SalesOpportunityResponseDTO> opportunitiesBySalesStage = service.getOpportunitiesBySalesStage(SalesStage.PROSPECTING);

        assertFalse(opportunitiesBySalesStage.isEmpty());
        verify(repository, times(1)).findBySalesStage(any(SalesStage.class));
    }

    @Test
    @DisplayName("getOpportunitiesBySalesStage() - Negative")
    void getOpportunitiesBySalesStageShouldThrowExceptionWhenSalesStageDoesNotExist() {
        when(repository.findBySalesStage(SalesStage.CLOSED_LOST)).thenAnswer((invocation -> {
            List<SalesOpportunity> opportunityList = new ArrayList<>();
            list.forEach(e -> {
                if (e.getSalesStage() == SalesStage.CLOSED_LOST) {
                    opportunityList.add(e);
                }
            });
            return opportunityList;
        }));

        assertThrows(NoSuchElementException.class, () -> service.getOpportunitiesBySalesStage(SalesStage.CLOSED_LOST));
        verify(repository, times(1)).findBySalesStage(any(SalesStage.class));
    }

    @Test
    @DisplayName("getOpportunitiesByEstimatedValue() - Positive")
    void getOpportunitiesByEstimatedValueShouldReturnListOfOpportunitiesWhenEstimatedValueExists() {
        when(repository.findByEstimatedValue(new BigDecimal("10000.0"))).thenAnswer((invocation -> {
            List<SalesOpportunity> opportunityList = new ArrayList<>();
            list.forEach(e -> {
                if (Objects.equals(e.getEstimatedValue(), new BigDecimal("10000.0"))) {
                    opportunityList.add(e);
                }
            });
            return opportunityList;
        }));

        List<SalesOpportunityResponseDTO> opportunitiesByEstimatedValue = service.getOpportunitiesByEstimatedValue(new BigDecimal("10000.0"));
        verify(repository, times(1)).findByEstimatedValue(new BigDecimal("10000.0"));
        assertFalse(opportunitiesByEstimatedValue.isEmpty());
    }

    @Test
    @DisplayName("getOpportunitiesByEstimatedValue() - Negative")
    void getOpportunitiesByEstimatedValueShouldThrowExceptionWhenEstimatedValueDoesNotExist() {
        BigDecimal bigDecimal = new BigDecimal("20000.0");
        when(repository.findByEstimatedValue(bigDecimal)).thenAnswer((invocation -> {
            List<SalesOpportunity> opportunityList = new ArrayList<>();
            list.forEach(e -> {
                if (e.getEstimatedValue().equals(bigDecimal)) {
                    opportunityList.add(e);
                }
            });
            return opportunityList;
        }));
        assertThrows(NoSuchElementException.class, () -> service.getOpportunitiesByEstimatedValue(bigDecimal));
        verify(repository, times(1)).findByEstimatedValue(bigDecimal);
    }

    @Test
    @DisplayName("getOpportunitiesByClosingDate() - Positive")
    void getOpportunitiesByClosingDateShouldReturnListOfOpportunitiesWhenClosingDateExists() {
        when(repository.findByClosingDate(LocalDate.of(2025, Month.MAY, 18))).thenAnswer((invocation -> {
            List<SalesOpportunity> opportunityList = new ArrayList<>();
            list.forEach(e -> {
                if (e.getClosingDate().equals(LocalDate.of(2025, Month.MAY, 18))) {
                    opportunityList.add(e);
                }
            });
            return opportunityList;
        }));

        List<SalesOpportunityResponseDTO> opportunitiesByClosingDate = service.getOpportunitiesByClosingDate(LocalDate.of(2025, Month.MAY, 18));
        verify(repository, times(1)).findByClosingDate(LocalDate.of(2025, Month.MAY, 18));
        assertFalse(opportunitiesByClosingDate.isEmpty());
    }

    @Test
    @DisplayName("getOpportunitiesByClosingDate() - Negative")
    void getOpportunitiesByClosingDateShouldThrowExceptionWhenClosingDateDoesNotExist() {
        LocalDate localDate = LocalDate.of(2025, Month.MAY, 20);
        when(repository.findByClosingDate(localDate)).thenAnswer((invocation -> {
            List<SalesOpportunity> opportunityList = new ArrayList<>();
            list.forEach(e -> {
                if (e.getClosingDate().equals(localDate)) {
                    opportunityList.add(e);
                }
            });
            return opportunityList;
        }));

        assertThrows(NoSuchElementException.class, () -> service.getOpportunitiesByClosingDate(localDate));
        verify(repository, times(1)).findByClosingDate(localDate);
    }

    @Test
    @DisplayName("getOpportunitiesByFollowUpReminder() - Positive")
    void getOpportunitiesByFollowUpReminderShouldReturnListOfOpportunitiesWhenFollowUpReminderExists() {
        when(repository.findByFollowUpReminder(LocalDate.of(2025, Month.APRIL, 18))).thenAnswer((invocation -> {
            List<SalesOpportunity> opportunityList = new ArrayList<>();
            list.forEach(e -> {
                if (e.getFollowUpReminder().equals(LocalDate.of(2025, Month.APRIL, 18))) {
                    opportunityList.add(e);
                }
            });
            return opportunityList;
        }));

        List<SalesOpportunityResponseDTO> opportunitiesByFollowUpReminder = service.getOpportunitiesByFollowUpReminder(LocalDate.of(2025, Month.APRIL, 18));
        verify(repository, times(1)).findByFollowUpReminder(LocalDate.of(2025, Month.APRIL, 18));
        assertFalse(opportunitiesByFollowUpReminder.isEmpty());
    }

    @Test
    @DisplayName("getOpportunitiesByFollowUpReminder() - Negative")
    void getOpportunitiesByFollowUpReminderShouldThrowExceptionWhenFollowUpReminderDoesNotExist() {
        LocalDate localDate = LocalDate.of(2025, Month.APRIL, 28);
        when(repository.findByFollowUpReminder(localDate)).thenAnswer((invocation -> {
            List<SalesOpportunity> opportunityList = new ArrayList<>();
            list.forEach(e -> {
                if (e.getFollowUpReminder().isEqual(localDate)) {
                    opportunityList.add(e);
                }
            });
            return opportunityList;
        }));

        assertThrows(NoSuchElementException.class, () -> service.getOpportunitiesByFollowUpReminder(localDate));
        verify(repository, times(1)).findByFollowUpReminder(localDate);
    }


    @Test
    @DisplayName("scheduleFollowUpReminder() - Positive")
    void scheduleFollowUpReminderShouldUpdateAndReturnOpportunityWhenValidInput() {
        SalesOpportunity salesOpportunity = list.getFirst();
        LocalDate localDate = LocalDate.of(2025, Month.APRIL, 18);
        when(repository.findById(1L)).thenReturn(Optional.ofNullable(salesOpportunity));
        assert salesOpportunity != null;
        when(repository.save(salesOpportunity)).thenAnswer(invocation -> {
            salesOpportunity.setFollowUpReminder(localDate);
            return salesOpportunity;
        });
        assertEquals(localDate, service.scheduleFollowUpReminder(1L, localDate).getFollowUpReminder());
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("scheduleFollowUpReminder() - Negative_InvalidDateTimeException")
    void scheduleFollowUpReminderShouldThrowExceptionWhenInvalidDateTime() {
        LocalDate localDate = LocalDate.of(2020, Month.JANUARY, 18);

        assertThrows(InvalidDateTimeException.class, () -> service.scheduleFollowUpReminder(1L, localDate));
        verify(repository, times(0)).findById(anyLong());
        verify(repository, times(0)).save(any());
    }

    @Test
    @DisplayName("scheduleFollowUpReminder() - Negative_InvalidOpportunityIDException")
    void scheduleFollowUpReminderShouldThrowExceptionWhenInvalidOpportunityId() {
        when(repository.findById(0L)).thenReturn(Optional.empty());
        LocalDate localDate = LocalDate.of(2026, Month.JANUARY, 18);
        assertThrows(InvalidOpportunityIdException.class, () -> service.scheduleFollowUpReminder(0L, localDate));
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(0)).save(any());
    }

    @Test
    @DisplayName("testDeleteByOpportunityID() - Positive")
    void deleteByOpportunityIdShouldRemoveOpportunityWhenIdExists() {
        SalesOpportunity obj = list.getFirst();
        when(repository.findById(1L)).thenAnswer((invocation -> {
            for (SalesOpportunity s : list) {
                if (s.getOpportunityID() == 1L) {
                    return Optional.of(s);
                }
            }
            return Optional.empty();
        }));
        doAnswer(invocation -> list.remove(obj)).when(repository).delete(obj);
        service.deleteByOpportunityID(1L);
        Optional<SalesOpportunity> salesOpportunity = repository.findById(1L);
        assertFalse(salesOpportunity.isPresent());
        verify(repository, times(1)).delete(obj);
        verify(repository, times(2)).findById(1L);
    }

    @Test
    @DisplayName("testDeleteByOpportunityID() - Negative")
    void deleteByOpportunityIdShouldThrowExceptionWhenIdDoesNotExist() {
        when(repository.findById(0L)).thenAnswer((invocation -> {
            for (SalesOpportunity s : list) {
                if (s.getOpportunityID() == 0L) {
                    return Optional.of(s);
                }
            }
            return Optional.empty();
        }));
        assertThrows(InvalidOpportunityIdException.class, () -> service.deleteByOpportunityID(0L));
        verify(repository, times(0)).delete(any(SalesOpportunity.class));
        verify(repository, times(1)).findById(0L);
    }

}