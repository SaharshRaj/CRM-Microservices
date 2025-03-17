

package com.crm.service;

import static org.junit.jupiter.api.Assertions.*; 
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;

import com.crm.mapper.CampaignMapper;
import com.crm.repository.CampaignRepository;
import com.crm.dto.CampaignDTO;
import com.crm.dto.EmailDTO;
import com.crm.entities.*;
import com.crm.enums.Type;
import com.crm.exception.CampaignNotFoundException;

@ExtendWith(MockitoExtension.class)
class CampaignServiceTestCase {
	
	@Mock
	private CampaignRepository campaignRepository;
	
	@Mock
	private CampaignMapper mapper;
	@InjectMocks
	private CampaignServiceImpl campaignServiceImpl;
	
	private CampaignDTO campaignDTO;
	private Campaign campaign;

	private JavaMailSender mailSender;
	@BeforeEach
	void setUp() throws Exception {
		 Campaign campaign=new Campaign();
		 campaign.setCampaignID(1L);
		 campaign.setName("Summer Sale");
		 campaign.setStartDate(LocalDate.of(2023, 06, 01));
		 campaign.setEndDate(LocalDate.of(2023, 06, 30));
		 campaign.setType(Type.EMAIL);
		 campaign.setCustomerInteractions(1500);
		 
		 
		 CampaignDTO campaignDTO=new CampaignDTO();
			campaignDTO.setCampaignID(1L);
			campaignDTO.setName("Summer Sale");
			campaignDTO.setStartDate(LocalDate.of(2023, 06, 01));
			campaignDTO.setEndDate(LocalDate.of(2023, 06, 30));
			campaignDTO.setType(Type.EMAIL);
			campaignDTO.setCustomerInteractions(1500);
	
	}

	@AfterEach
	void tearDown() throws Exception {
		campaignRepository=null;
		campaignServiceImpl=null;
		
	}

	@Test
	void test_getAllCampaigns_positive() throws CampaignNotFoundException{
		when(campaignRepository.findAll()).thenReturn(Arrays.asList(campaign));
		try {
			List<CampaignDTO> actual=campaignServiceImpl.retrieveAllCampaigns();
			verify(campaignRepository,times(1)).findAll();
			assertEquals(1,actual.size());
		}catch(CampaignNotFoundException e) {
			assertFalse(true);
		}
			
	}
	@Test
	void test_getAllCampaigns_emptyList_negative() {
		when(campaignRepository.findAll()).thenReturn(Arrays.asList());
		try {
			List<CampaignDTO> actual=campaignServiceImpl.retrieveAllCampaigns();
			assertFalse(actual.size()>0);
		}catch(CampaignNotFoundException e) {
			assertTrue(true);
		}
	}
	
	@Test
	void test_getAllCampaigns_whenRepositoryReturnsMoreThanEntity_positive() throws CampaignNotFoundException {
		when(campaignRepository.findAll()).thenAnswer((invocation)->{
			Campaign campaign1=new Campaign();
			campaign1.setCampaignID(1L);
			campaign1.setName("Summer Sale");
			campaign1.setStartDate(LocalDate.of(2023, 06, 01));
			campaign1.setEndDate(LocalDate.of(2023, 06, 30));
			campaign1.setType(Type.EMAIL);
			
			Campaign campaign2=new Campaign();
			campaign2.setCampaignID(1L);
			campaign2.setName("Summer Sale");
			campaign2.setStartDate(LocalDate.of(2023, 06, 01));
			campaign2.setEndDate(LocalDate.of(2023, 06, 30));
			campaign2.setType(Type.EMAIL);
			return Arrays.asList(campaign1,campaign2);
			});
		try {
			List<CampaignDTO> actual=campaignServiceImpl.retrieveAllCampaigns();
			verify(campaignRepository,times(1)).findAll();
			assertTrue(actual.size()>1);
		}catch(CampaignNotFoundException e) {
			assertFalse(true);
		}
	}
	
	@Test
	void test_getAllCampaigns_whenRepositoryReturnsMoreThanEntity_negative() {
		when(campaignRepository.findAll()).thenAnswer((invocation)->{
			return Arrays.asList();
		});
		//assertThrows(CampaignNotFoundException.class,()->campaignServiceImpl.retrieveAllCampaigns());
		try {
			List<CampaignDTO> actual=campaignServiceImpl.retrieveAllCampaigns();
			assertFalse(actual.size()>0);
		}catch(CampaignNotFoundException e) {
			assertTrue(true);
		}
	}
	
	@Test
	void test_saveCampaigns_positive() throws CampaignNotFoundException{
		CampaignDTO campaignDTO1=new CampaignDTO();
		campaignDTO1.setCampaignID(1L);
		campaignDTO1.setName("Summer Sale");
		campaignDTO1.setStartDate(LocalDate.of(2023, 06, 01));
		campaignDTO1.setEndDate(LocalDate.of(2023, 06, 30));
		campaignDTO1.setType(Type.EMAIL);
		campaignDTO1.setCustomerInteractions(1500);
		
		Campaign campaign=new Campaign();
		campaign.setCampaignID(1L);
		campaign.setName("Summer Sale");
		campaign.setStartDate(LocalDate.of(2023, 06, 01));
		campaign.setEndDate(LocalDate.of(2023, 06, 30));
		campaign.setType(Type.EMAIL);
		campaign.setCustomerInteractions(1500);
		when(mapper.mapToCampaign(any())).thenReturn(campaign);
		when(campaignRepository.save(any())).thenReturn(campaign);
		boolean actual=campaignServiceImpl.saveCampaigns(campaignDTO1);
		
		assertTrue(actual);
		
	}
	@Test
	void test_saveCamapigns_negative() {
		CampaignDTO campaignDTO=new CampaignDTO();
		Campaign campaign=new Campaign();
		when(mapper.mapToCampaign(any())).thenReturn(campaign);
		
		when(campaignRepository.save(any())).thenThrow(new CampaignNotFoundException("Campaign not saved"));
		assertThrows(CampaignNotFoundException.class,()->campaignServiceImpl.saveCampaigns(campaignDTO));
	}
	@Test 
	void test_getCampaignById_positive() throws CampaignNotFoundException{
	when(campaignRepository.findById(1L)).thenAnswer(new Answer<Optional<Campaign>>() {
		@Override
		public Optional<Campaign> answer(InvocationOnMock invocation) throws Throwable {
			// TODO Auto-generated method stub
			Optional<Campaign> optionalCampaign=null;
			Long campaignId=(Long) invocation.getArgument(0);
			if(campaignId==1L) {
				 Campaign campaign=new Campaign();
				 campaign.setCampaignID(1L);
				 campaign.setName("Summer Sale");
				 campaign.setStartDate(LocalDate.of(2023, 06, 01));
				 campaign.setEndDate(LocalDate.of(2023, 06, 30));
				 campaign.setType(Type.EMAIL);
				 campaign.setCustomerInteractions(1500);
				 optionalCampaign=Optional.of(campaign);
			}
			return optionalCampaign;
		}
		
	});
	    CampaignDTO campaignDTO=new CampaignDTO();
	    campaignDTO.setCampaignID(1L);
	    campaignDTO.setName("Summer Sale");
	    campaignDTO.setStartDate(LocalDate.of(2023, 06, 01));
	    campaignDTO.setEndDate(LocalDate.of(2023, 06, 30));
	    campaignDTO.setType(Type.EMAIL);
	    campaignDTO.setCustomerInteractions(1500);
	    
	    when(mapper.mapToDTO(any())).thenReturn(campaignDTO);
		CampaignDTO actual=campaignServiceImpl.getCampaignById(1L);
		
		assertEquals(actual.getCampaignID(),1L);
	
		
	}
	@Test
	void test_getCampaignById_negative() {
		when(campaignRepository.findById(any())).thenAnswer((invocation)->Optional.empty());
		assertThrows(CampaignNotFoundException.class,()->campaignServiceImpl.getCampaignById(1L));
	}
	@Test
	void test_updateCampaign_positive() throws CampaignNotFoundException{
		
		Campaign campaign=new Campaign();
		campaign.setCampaignID(1L);
		campaign.setName("Summer Sale");
		campaign.setStartDate(LocalDate.of(2023, 06, 01));
		campaign.setEndDate(LocalDate.of(2023, 06, 30));
		campaign.setType(Type.EMAIL);
		campaign.setCustomerInteractions(1500);
		when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaign));
		CampaignDTO updatedCampaignDTO=new CampaignDTO();
		updatedCampaignDTO.setName("Summer Sale Bonanza");
		
		CampaignDTO campaignDTO=new CampaignDTO();
		campaignDTO.setCampaignID(1L);
		campaignDTO.setName("Summer Sale Bonanza");
		campaignDTO.setStartDate(LocalDate.of(2023, 06, 01));
		campaignDTO.setEndDate(LocalDate.of(2023, 06, 30));
		campaignDTO.setType(Type.EMAIL);
		campaignDTO.setCustomerInteractions(1500);

		when(mapper.mapToDTO(campaign)).thenReturn(updatedCampaignDTO);
		CampaignDTO actual=campaignServiceImpl.updateCampaign(1L, updatedCampaignDTO);
		assertEquals("Summer Sale Bonanza",actual.getName());
		
		
	}
	@Test
	void test_updateCampaigns_negative() {
		when(campaignRepository.findById(1L)).thenReturn(Optional.empty());
		assertThrows(CampaignNotFoundException.class,()->campaignServiceImpl.updateCampaign(1L, new CampaignDTO()));
	}
	@Test
	void test_deleteCampaign_positive() throws CampaignNotFoundException{
		when(campaignRepository.findById(1L)).thenAnswer(new Answer<Optional<Campaign>>() {
			@Override
			public Optional<Campaign> answer(InvocationOnMock invocation) throws Throwable{
				Optional<Campaign> optionalCampaign=null;
	        	 Long campaignId=(Long)invocation.getArgument(0);
	        	 if(campaignId==1L) {
	        		 Campaign campaign=new Campaign();
	        		 campaign.setCampaignID(1L);
	        		 campaign.setName("Summer Sale");
	        		 campaign.setStartDate(LocalDate.of(2023, 06, 01));
	        		 campaign.setEndDate(LocalDate.of(2023, 06, 30));
	        		 campaign.setType(Type.EMAIL);
	        		 campaign.setCustomerInteractions(1500);
	        		 optionalCampaign=Optional.of(campaign);
	        	 }
	        	 return optionalCampaign;
			}
		});
		try {
			boolean actual=campaignServiceImpl.deleteCampaign(1L);
			verify(campaignRepository,times(1)).deleteById(1L);
			assertTrue(actual);
		}catch(CampaignNotFoundException e) {
			assertTrue(false);
		}
	}
	@Test
	void test_deleteCamapign_negative() {
		when(campaignRepository.findById(1L)).thenReturn(Optional.empty());
		assertThrows(CampaignNotFoundException.class,()->campaignServiceImpl.deleteCampaign(1L));
	}
	@Test
    void test_getByType_validType_returnsCampaigns() {
        when(campaignRepository.findByType(Type.EMAIL)).thenReturn(Arrays.asList(campaign));
        when(mapper.mapToDTO(any())).thenReturn(campaignDTO);

        List<CampaignDTO> actual = campaignServiceImpl.getByType(Type.EMAIL);

        assertEquals(1, actual.size());
        assertEquals(campaignDTO, actual.get(0));
    }

    @Test
    void test_getByType_noCampaignsOfType_returnsEmptyList() {
        when(campaignRepository.findByType(Type.EMAIL)).thenReturn(Arrays.asList());

        List<CampaignDTO> actual = campaignServiceImpl.getByType(Type.EMAIL);

        assertEquals(0, actual.size());
    }
}