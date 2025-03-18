package com.crm.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.crm.dto.CampaignDTO;
import com.crm.entities.Campaign;
import com.crm.enums.Type;
import com.crm.exception.CampaignNotFoundException;
import com.crm.mapper.CampaignMapper;
import com.crm.repository.CampaignRepository;
import com.crm.service.CampaignServiceImpl;

/**
 * Test class for {@link CampaignControllerImpl}.
 */
@DataJpaTest
@ActiveProfiles("test")
class CampaignControllerTestCase {

    @Mock
    private CampaignServiceImpl campaignServiceImpl;

    @Mock
    private CampaignRepository campaignRepository;

    @MockitoBean
    private CampaignMapper campaignMapper;

    @InjectMocks
    private CampaignControllerImpl campaignControllerImpl;

    @MockitoBean
    private JavaMailSender mailSender;

    /**
     * Cleans up resources after each test.
     */
    @AfterEach
    void tearDown() {
        campaignServiceImpl = null;
        campaignControllerImpl = null;
    }

    /**
     * Tests the positive scenario for retrieving all campaigns.
     * Verifies that the controller returns a list of campaigns with HTTP status OK.
     */
    @Test
    void testGetAllCampaigns_positive() {
        try {
            CampaignDTO campaignDTO = createCampaignDTO();
            Campaign campaign = createCampaign();
            when(campaignRepository.findAll()).thenReturn(Arrays.asList(campaign));
            when(campaignMapper.mapToDTO(any())).thenReturn(campaignDTO);
            when(campaignServiceImpl.retrieveAllCampaigns()).thenReturn(Arrays.asList(campaignDTO));
            ResponseEntity<List<CampaignDTO>> actual = campaignControllerImpl.getAllCampaigns();
            assertTrue(actual.getBody().size() > 0);
        } catch (CampaignNotFoundException e) {
            assertTrue(false);
        }
    }

    /**
     * Tests the positive scenario for retrieving a campaign by ID.
     * Verifies that the controller returns the correct campaign with HTTP status OK.
     * @throws CampaignNotFoundException if campaign isn't found
     */
    @Test
    void testGetCampaignById_positive() throws CampaignNotFoundException {
        Long campaignId = 1L;
        CampaignDTO expectedCampaign = new CampaignDTO();
        expectedCampaign.setCampaignID(campaignId);
        when(campaignServiceImpl.getCampaignById(campaignId)).thenReturn(expectedCampaign);
        ResponseEntity<?> response = campaignControllerImpl.getCampaignById(campaignId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCampaign, response.getBody());
    }

    /**
     * Tests the negative scenario for retrieving a campaign by ID.
     * Verifies that the controller returns HTTP status NOT_FOUND when the campaign is not found.
     * @throws CampaignNotFoundException if campaign isn't found
     */
    @Test
    void testGetCampaignById_negative() throws CampaignNotFoundException {
        Long campaignId = 1L;
        String errorMessage = null;
        when(campaignServiceImpl.getCampaignById(campaignId)).thenThrow(new CampaignNotFoundException(errorMessage));
        ResponseEntity<CampaignDTO> response = campaignControllerImpl.getCampaignById(campaignId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Tests the negative scenario for retrieving all campaigns.
     * Verifies that the controller returns HTTP status NOT_FOUND when no campaigns are found.
     */
    @Test
    void testGetAllCampaigns_negative() {
        when(campaignServiceImpl.retrieveAllCampaigns()).thenThrow(new CampaignNotFoundException("Campaign Not found"));
        ResponseEntity<?> actual = campaignControllerImpl.getAllCampaigns();
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }

    /**
     * Tests the positive scenario for creating a campaign.
     * Verifies that the controller returns HTTP status CREATED.
     */
    @Test
    void testCreateCampaign_positive() {
        try {
            CampaignDTO campaignDTO = createCampaignDTO();
            Campaign campaign = createCampaign();
            when(campaignMapper.mapToCampaign(any(CampaignDTO.class))).thenReturn(campaign);
            when(campaignServiceImpl.createCampaign(any(CampaignDTO.class))).thenReturn(campaignDTO);
            ResponseEntity<?> actual = campaignControllerImpl.createCampaign(campaignDTO);
            assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        } catch (CampaignNotFoundException e) {
            assertTrue(false);
        }
    }

    /**
     * Tests the negative scenario for creating a campaign.
     * Verifies that the controller returns HTTP status NOT_ACCEPTABLE when the campaign creation fails.
     */
    @Test
    void testCreateCampaign_negative() {
        CampaignDTO campaignDTO = new CampaignDTO();
        when(campaignServiceImpl.createCampaign(any(CampaignDTO.class))).thenThrow(new CampaignNotFoundException("Invalid campaign type"));
        ResponseEntity<CampaignDTO> actual = campaignControllerImpl.createCampaign(campaignDTO);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, actual.getStatusCode());
        assertEquals(campaignDTO, actual.getBody());
    }

    /**
     * Tests the positive scenario for updating a campaign.
     * Verifies that the controller returns HTTP status OK and the updated campaign.
     */
    @Test
    void testUpdateCampaign_positive() {
        try {
            CampaignDTO campaignDTO = createCampaignDTO();
            when(campaignServiceImpl.updateCampaign(1L, campaignDTO)).thenReturn(campaignDTO);
            ResponseEntity<CampaignDTO> actual = campaignControllerImpl.updateCampaign(1L, campaignDTO);
            assertEquals(HttpStatus.OK, actual.getStatusCode());
            assertEquals(campaignDTO, actual.getBody());
        } catch (CampaignNotFoundException e) {
            assertTrue(false);
        }
    }

    /**
     * Tests the negative scenario for updating a campaign.
     * Verifies that the controller returns HTTP status NOT_MODIFIED when the campaign update fails.
     */
    @Test
    void testUpdateCampaign_negative() {
        CampaignDTO campaignDTO = createCampaignDTO();
        when(campaignServiceImpl.updateCampaign(1L, campaignDTO)).thenThrow(new CampaignNotFoundException("Campaign Not Found with the ID"));
        ResponseEntity<CampaignDTO> actual = campaignControllerImpl.updateCampaign(1L, campaignDTO);
        assertEquals(HttpStatus.NOT_MODIFIED, actual.getStatusCode());
    }

    /**
     * Tests the positive scenario for deleting a campaign.
     * Verifies that the controller returns HTTP status ACCEPTED.
     */
    @Test
    void testDeleteCampaign_positive() {
        try {
            when(campaignServiceImpl.deleteCampaign(1L)).thenReturn(true);
            ResponseEntity<CampaignDTO> actual = campaignControllerImpl.deleteCampaign(1L);
            assertEquals(HttpStatus.ACCEPTED, actual.getStatusCode());
        } catch (CampaignNotFoundException e) {
            assertTrue(false);
        }
    }

    /**
     * Tests the negative scenario for deleting a campaign.
     * Verifies that the controller returns HTTP status NOT_FOUND when the campaign deletion fails.
     */
    @Test
    void testDeleteCampaign_negative() {
        when(campaignServiceImpl.deleteCampaign(1L)).thenThrow(new CampaignNotFoundException("Campaign not found"));
        ResponseEntity<CampaignDTO> actual = campaignControllerImpl.deleteCampaign(1L);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }

    /**
     * Helper method to create a sample {@link CampaignDTO}.
     * @return a {@link CampaignDTO} object.
     */
    private CampaignDTO createCampaignDTO() {
        CampaignDTO campaignDTO = new CampaignDTO();
        campaignDTO.setCampaignID(1L);
        campaignDTO.setName("Summer Sale");
        campaignDTO.setStartDate(LocalDate.of(2023, 06, 01));
        campaignDTO.setEndDate(LocalDate.of(2023, 06, 30));
        campaignDTO.setType(Type.EMAIL);
        campaignDTO.setCustomerInteractions(1500);
        campaignDTO.setTrackingUrl("http://localhost:3004/api/marketing/7/track");
        return campaignDTO;
    }

    /**
     * Helper method to create a sample {@link Campaign}.
     * @return a {@link Campaign} object.
     */
    private Campaign createCampaign() {
        Campaign campaign = new Campaign();
        campaign.setCampaignID(1L);
        campaign.setName("Summer Sale");
        campaign.setStartDate(LocalDate.of(2023, 06, 01));
        campaign.setEndDate(LocalDate.of(2023, 06, 30));
        campaign.setType(Type.EMAIL);
        campaign.setCustomerInteractions(1500);
        campaign.setTrackingUrl("http://localhost:3004/api/marketing/7/track");
        return campaign;
    }

    /**
     * Tests the positive scenario for creating multiple campaigns.
     * Verifies that the controller returns HTTP status CREATED and the list of created campaigns.
     * @throws CampaignNotFoundException if campaign isn't found
     */
    @Test
    void test_createCampaigns_success() throws CampaignNotFoundException {
        // Arrange
        List<CampaignDTO> inputDTOs = new ArrayList<>();
        CampaignDTO campaignDTO1 = createCampaignDTO();
        CampaignDTO campaignDTO2 = createCampaignDTO();
        inputDTOs.add(campaignDTO1);
        inputDTOs.add(campaignDTO2);
        List<CampaignDTO> createdDTOs = new ArrayList<>(inputDTOs); // Same list for simplicity
        when(campaignServiceImpl.createCampaigns(inputDTOs)).thenReturn(createdDTOs);
        ResponseEntity<List<CampaignDTO>> response = campaignControllerImpl.createCampaigns(inputDTOs);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdDTOs, response.getBody());
        verify(campaignServiceImpl).createCampaigns(inputDTOs);
    }

    /**
     * Tests the scenario where creating multiple campaigns results in a CampaignNotFoundException.
     * Verifies that the controller returns HTTP status BAD_REQUEST.
     * @throws CampaignNotFoundException if campaign isn't found
     */
    @Test
    void test_createCampaigns_campaignNotFoundException() throws CampaignNotFoundException {
        // Arrange
        List<CampaignDTO> inputDTOs = new ArrayList<>();
        when(campaignServiceImpl.createCampaigns(inputDTOs)).thenThrow(new CampaignNotFoundException("Campaign not found"));
        ResponseEntity<List<CampaignDTO>> response = campaignControllerImpl.createCampaigns(inputDTOs);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(campaignServiceImpl).createCampaigns(inputDTOs);
    }

    /**
     * Tests the scenario where creating multiple campaigns results in a NullPointerException.
     * Verifies that the controller returns HTTP status BAD_REQUEST.
     * @throws CampaignNotFoundException if campaign isn't found
     */
    @Test
    void test_createCampaigns_nullPointerException() throws CampaignNotFoundException {
        List<CampaignDTO> inputDTOs = new ArrayList<>();
        when(campaignServiceImpl.createCampaigns(inputDTOs)).thenThrow(new NullPointerException("Null pointer exception"));
        ResponseEntity<List<CampaignDTO>> response = campaignControllerImpl.createCampaigns(inputDTOs);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(campaignServiceImpl).createCampaigns(inputDTOs);
    }

    /**
     * Tests the successful tracking of a campaign click.
     * Verifies that the controller returns HTTP status FOUND and the correct redirect location.
     * @throws CampaignNotFoundException if campaign isn't found
     */
    @Test
    void test_trackCampaignClick_success() throws CampaignNotFoundException {
        Long campaignId = 1L;
        when(campaignServiceImpl.trackCampaignClick(campaignId)).thenReturn("http://localhost:3004/success.html");
        ResponseEntity<Void> response = campaignControllerImpl.trackCampaignClick(campaignId);
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(URI.create("/success.html"), response.getHeaders().getLocation());
        verify(campaignServiceImpl).trackCampaignClick(campaignId);
    }

    /**
     * Tests the scenario where tracking a campaign click results in a CampaignNotFoundException.
     * Verifies that the controller returns HTTP status NOT_FOUND.
     * @throws CampaignNotFoundException if campaign isn't found
     */
    @Test
    void test_trackCampaignClick_campaignNotFound() throws CampaignNotFoundException {
        Long campaignId = 1L;
        doThrow(new CampaignNotFoundException("Campaign not found")).when(campaignServiceImpl).trackCampaignClick(campaignId);
        ResponseEntity<Void> response = campaignControllerImpl.trackCampaignClick(campaignId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(campaignServiceImpl).trackCampaignClick(campaignId);
    }
}