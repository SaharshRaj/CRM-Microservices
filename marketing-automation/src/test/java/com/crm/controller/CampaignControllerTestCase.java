package com.crm.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.crm.dto.CampaignDTO;
import com.crm.entities.Campaign;
import com.crm.enums.Type;
import com.crm.exception.CampaignNotFoundException;
import com.crm.mapper.CampaignMapper;
import com.crm.repository.CampaignRepository;
import com.crm.service.CampaignServiceImpl;

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
	
	 @MockBean
	 private JavaMailSender mailSender;
	 
	 private MockMvc mockMvc;

	@BeforeEach
	void setUp() throws Exception {
		mockMvc=MockMvcBuilders.standaloneSetup(campaignControllerImpl).build();
	}

	@AfterEach
	void tearDown() throws Exception {
		campaignServiceImpl=null;
		campaignControllerImpl=null;
		mockMvc=null;
	}

	@Test
	void testGetAllCampaigns_positive() {
		try {
			
		CampaignDTO campaignDTO=createCampaignDTO();
		Campaign campaign=createCampaign();
		when(campaignRepository.findAll()).thenReturn(Arrays.asList(campaign));
		when(campaignMapper.mapToDTO(any())).thenReturn(campaignDTO);
		when(campaignServiceImpl.retrieveAllCampaigns()).thenReturn(Arrays.asList(campaignDTO));
		ResponseEntity<List<CampaignDTO>>actual = campaignControllerImpl.getAllCampaigns();
		assertTrue(actual.getBody().size()>0);
		
		
		}catch(CampaignNotFoundException e) {
			assertTrue(false);
		}
		
		
	}
	@Test
    public void testGetCampaignById_positive() throws CampaignNotFoundException {
        // Arrange
        Long campaignId = 1L;
        CampaignDTO expectedCampaign = new CampaignDTO(); // Create a sample CampaignDTO
        expectedCampaign.setCampaignID(campaignId);
        when(campaignServiceImpl.getCampaignById(campaignId)).thenReturn(expectedCampaign);

        // Act
        ResponseEntity<?> response = campaignControllerImpl.getCampaignById(campaignId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCampaign, response.getBody());
    }

    @Test
    public void testGetCampaignById_negative() throws CampaignNotFoundException {
        // Arrange
        Long campaignId = 1L;
        String errorMessage = "Campaign not found";
        when(campaignServiceImpl.getCampaignById(campaignId)).thenThrow(new CampaignNotFoundException(errorMessage));

        // Act
        ResponseEntity<?> response = campaignControllerImpl.getCampaignById(campaignId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }
	@Test
	void testGetAllCampaigns_negative() {
		when(campaignServiceImpl.retrieveAllCampaigns()).thenThrow(new CampaignNotFoundException("Campaign Not found"));
		ResponseEntity<?> actual=campaignControllerImpl.getAllCampaigns();
		assertEquals(HttpStatus.NOT_FOUND,actual.getStatusCode());
		
	}
	@Test
    void testCreateCampaign_positive() {
        try {
            CampaignDTO campaignDTO = createCampaignDTO();
            Campaign campaign = createCampaign();

            when(campaignMapper.mapToCampaign(any(CampaignDTO.class))).thenReturn(campaign);
            when(campaignServiceImpl.createCampaign(any(CampaignDTO.class))).thenReturn(campaignDTO);

            ResponseEntity<?> actual = campaignControllerImpl.createCampaign(campaignDTO);

            assertEquals(HttpStatus.CREATED, actual.getStatusCode());
            //assertEquals("Campaign created successfully.", actual.getBody());
        } catch (CampaignNotFoundException e) {
            assertTrue(false);
        }
    }
	 @Test
	 void testCreateCampaign_negative() {
		    CampaignDTO campaignDTO = createCampaignDTO();
		    when(campaignServiceImpl.createCampaign(any(CampaignDTO.class))).thenThrow(new CampaignNotFoundException("Invalid campaign type"));

		    ResponseEntity<?> actual = campaignControllerImpl.createCampaign(campaignDTO);

		    assertEquals(HttpStatus.NOT_ACCEPTABLE, actual.getStatusCode());
		    assertEquals("Invalid campaign type", actual.getBody());
		}
	 
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
	 @Test
	    void testUpdateCampaign_negative() {
	        CampaignDTO campaignDTO = createCampaignDTO();
	        when(campaignServiceImpl.updateCampaign(1L, campaignDTO)).thenThrow(new CampaignNotFoundException("Campaign Not Found with the ID"));

	        ResponseEntity<CampaignDTO> actual = campaignControllerImpl.updateCampaign(1L, campaignDTO);

	        assertEquals(HttpStatus.NOT_MODIFIED, actual.getStatusCode());
	    }
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
	 @Test
	    void testDeleteCampaign_negative() {
	        when(campaignServiceImpl.deleteCampaign(1L)).thenThrow(new CampaignNotFoundException("Campaign not found"));

	        ResponseEntity<CampaignDTO> actual = campaignControllerImpl.deleteCampaign(1L);

	        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
	    }
	private CampaignDTO createCampaignDTO() {
		CampaignDTO campaignDTO=new CampaignDTO();
		campaignDTO.setCampaignID(1L);
		campaignDTO.setName("Summer Sale");
		campaignDTO.setStartDate(LocalDate.of(2023, 06, 01));
		campaignDTO.setEndDate(LocalDate.of(2023, 06, 30));
		campaignDTO.setType(Type.EMAIL);
		campaignDTO.setCustomerInteractions(1500);
		return campaignDTO;
	}
	private Campaign createCampaign() {
		Campaign campaign=new Campaign();
		campaign.setCampaignID(1L);
		campaign.setName("Summer Sale");
		campaign.setStartDate(LocalDate.of(2023, 06, 01));
		campaign.setEndDate(LocalDate.of(2023, 06, 30));
		campaign.setType(Type.EMAIL);
		campaign.setCustomerInteractions(1500);
		return campaign;
	}

}
