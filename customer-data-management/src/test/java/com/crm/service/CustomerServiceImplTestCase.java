package com.crm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.crm.enums.Interest;
import com.crm.enums.Region;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crm.dto.CustomerProfileDTO;
import com.crm.entities.CustomerProfile;
import com.crm.enums.PurchasingHabits;
import com.crm.exception.ResourceNotFoundException;
import com.crm.mapper.CustomerProfileMapper;
import com.crm.repository.CustomerProfileRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTestCase {
	
	@Mock
	private CustomerProfileRepository customerProfileRepository;
	
	@Mock
	private CustomerProfileMapper customerProfileMapper;
	
	@InjectMocks
	private CustomerServiceImpl customerServiceImpl;
	

	private List<CustomerProfileDTO> customerProfilesDTOs;
	private List<CustomerProfile> customerProfiles;

	@BeforeEach
	void setUp() throws Exception {
		
		 CustomerProfile customerProfile1 = CustomerProfile.builder()
	                .customerID(1L)
	                .name("John Doe")
	                .emailId("john@example.com")
	                .phoneNumber("1234567890")
	                .build();

		 CustomerProfile customerProfile2 = CustomerProfile.builder()
	                .customerID(1L)
	                .customerID(2L)
	                .name("Jane Doe")
	                .emailId("jane@example.com")
	                .phoneNumber("0987654321")
	                .build();
		 
		 customerProfiles = Arrays.asList(customerProfile1, customerProfile2);
		 
		MockitoAnnotations.openMocks(this);

	}

	@AfterEach
	void tearDown() throws Exception {
		customerProfileRepository = null;
		customerServiceImpl = null;
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testRetrieveAllProfiles_Positive() {

		when(customerProfileRepository.findAll()).thenReturn(customerProfiles);
        assertFalse(customerServiceImpl.retrieveAllProfiles().isEmpty());
        verify(customerProfileRepository, times(1)).findAll();
	}

	@Test
	void testRetrieveAllProfiles_Negative() {

		when(customerProfileRepository.findAll()).thenReturn(List.of());
		assertThrows(ResourceNotFoundException.class, () ->{
			customerServiceImpl.retrieveAllProfiles();
		});
		verify(customerProfileRepository, times(1)).findAll();
	}

	@Test
	void testAddCustomer_Profile(){
		CustomerProfile customerProfile = customerProfiles.getFirst();
		CustomerProfileDTO customerProfileDTO = CustomerProfileDTO.builder().customerID(1L)
				.name("John Doe")
				.emailId("john@example.com")
				.phoneNumber("1234567890")
				.build();

		when(customerProfileRepository.save(customerProfile)).thenReturn(customerProfile);
		when(customerProfileMapper.mapToCustomer(customerProfileDTO)).thenReturn(customerProfile);
		customerServiceImpl.addCustomerProfile(customerProfileDTO);
		verify(customerProfileRepository, times(1)).save(customerProfile);
	}

	@Test
	void testAddCustomerProfile_Negative(){

		assertThrows(ResourceNotFoundException.class,()->{
			customerServiceImpl.addCustomerProfile(null);
		});

	}

	@Test
    void testGetCustomerById_Positive() {

        CustomerProfileDTO customerDTO = CustomerProfileDTO.builder()
                .customerID(1L)
                .name("John Doe")
                .emailId("john@example.com")
                .phoneNumber("1234567890")
                .build();

        when(customerProfileRepository.findById(1L)).thenAnswer((invocation -> {
            for(CustomerProfile c : customerProfiles){
                if(c.getCustomerID()== 1L){
                    return Optional.of(c);
                }
            }
                return Optional.empty();
            }));
        
        
        when(customerProfileMapper.mapToDTO(customerProfiles.get(0))).thenReturn(customerDTO);

        CustomerProfileDTO result = customerServiceImpl.getCustomerById(1L);

        assertEquals(customerDTO, result);
        
        verify(customerProfileRepository, times(1)).findById(1L);
    }
    
    @Test
    void testGetCustomerById_Negative() throws ResourceNotFoundException{
    	
    	when(customerProfileRepository.findById(1L)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, ()->{
			customerServiceImpl.getCustomerById(1l);
		});
    }
    
    @Test
    void testUpdateCustomer_Positive() throws ResourceNotFoundException {
        

        CustomerProfileDTO customerDTO = CustomerProfileDTO.builder()
                .customerID(1L)
                .name("Thamzhini")
                .emailId("Thamz@example.com")
                .phoneNumber("9008006005")
                .build();

        when(customerProfileRepository.findById(1L)).thenReturn(Optional.of(customerProfiles.get(0)));
        when(customerProfileRepository.save(customerProfiles.get(0))).thenReturn(customerProfiles.get(0));
        when(customerProfileMapper.mapToDTO(customerProfiles.get(0))).thenReturn(customerDTO);

        CustomerProfileDTO result = customerServiceImpl.updateCustomer(1L, customerDTO);

        assertEquals(customerDTO, result);
    }
    
    @Test
    void testUpdateCustomer_Negative() throws ResourceNotFoundException {
    	 CustomerProfileDTO customerDTO = CustomerProfileDTO.builder()
                 .customerID(1L)
                 .name("Thamzhini")
                 .emailId("Thamz@example.com")
                 .phoneNumber("9008006005")
                 .build();
    	when(customerProfileRepository.findById(1L)).thenReturn(Optional.empty());
    	assertThrows(ResourceNotFoundException.class, ()->{
    		customerServiceImpl.updateCustomer(1L, customerDTO);
		});
    	verify(customerProfileRepository,never()).save(any(CustomerProfile.class));
    }

    @Test
    void testDeleteCustomer_Positive() throws ResourceNotFoundException {
        

        when(customerProfileRepository.findById(1L)).thenReturn(Optional.of(customerProfiles.get(0)));

        customerServiceImpl.deleteCustomer(1L);

        verify(customerProfileRepository, times(1)).deleteById(1L);
    }
    @Test
    void testDeleteCustomer_Negative() throws ResourceNotFoundException {
        

        when(customerProfileRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,()->{
        	
        	customerServiceImpl.deleteCustomer(1L);
        });

    }
    
    @Test
    void testSearchCustomerBasedOnEmailId_Positive() {
    	CustomerProfile customer2 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Thamz123@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList()).build();
    	CustomerProfile customer3 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Thamz123@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList()).build();
    	List<CustomerProfile> emailList= List.of(customer2, customer3);
    	when(customerProfileRepository.findAllByEmailId("Thamz123@example.com")).thenReturn(emailList);
    	
    	List<CustomerProfileDTO> foundEmailList = customerServiceImpl.searchCustomerBasedOnEmailId("Thamz123@example.com");
    	assertEquals(emailList.size(), foundEmailList.size()); 
    }

    @Test
    void testSearchCustomerBasedOnEmailId_Negative() {
    	
    	List<CustomerProfile> emailList= List.of();
    	when(customerProfileRepository.findAllByEmailId("Thamz123@example.com")).thenReturn(emailList);
    	
    	assertThrows(ResourceNotFoundException.class, ()->{
    		List<CustomerProfileDTO> foundEmailList = customerServiceImpl.searchCustomerBasedOnEmailId("Thamz123@example.com");
    	});
    }
    
    @Test
    void testSearchCustomerBasedOnName_Positive() {
    	CustomerProfile customer2 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Thamz123@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList()).build();
    	CustomerProfile customer3 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Thamz123@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList()).build();
    	List<CustomerProfile> nameList= List.of(customer2, customer3);
    	when(customerProfileRepository.findAllByNames("Thamizhini")).thenReturn(nameList);
    	
    	List<CustomerProfileDTO> foundNameList = customerServiceImpl.searchCustomerBasedOnName("Thamizhini");
    	assertEquals(nameList.size(), foundNameList.size()); 
    }
    
    @Test
    void testSearchCustomerBasedOnName_Negative() {
    	
    	List<CustomerProfile> nameList= List.of();
    	when(customerProfileRepository.findAllByNames("Thamizhini")).thenReturn(nameList);
    	
    	assertThrows(ResourceNotFoundException.class, ()->{
    		List<CustomerProfileDTO> foundNameList = customerServiceImpl.searchCustomerBasedOnName("Thamizhini");
    	});
    }
    
    @Test
    void testSearchCustomerBasedOnPhoneNumber_Positive() {
    	CustomerProfile customer2 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Thamz123@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList()).build();
    	CustomerProfile customer3 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Thamz123@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList()).build();
    	List<CustomerProfile> phoneNumberList= List.of(customer2, customer3);
    	when(customerProfileRepository.findAllByPhoneNumber("7776665552")).thenReturn(phoneNumberList);
    	
    	List<CustomerProfileDTO> foundPhoneNumberList = customerServiceImpl.searchCustomerBasedOnPhoneNumber("7776665552");
    	assertEquals(phoneNumberList.size(), foundPhoneNumberList.size()); 
    }
    
    @Test
    void testSearchCustomerBasedOnPhoneNumber_Negative() {
    	
    	List<CustomerProfile> phoneNumberList= List.of();
    	when(customerProfileRepository.findAllByPhoneNumber("7776665552")).thenReturn(phoneNumberList);
    	
    	assertThrows(ResourceNotFoundException.class, ()->{
    		List<CustomerProfileDTO> foundPhoneNumberList = customerServiceImpl.searchCustomerBasedOnPhoneNumber("7776665552");
    	});
    }

	@Test
	void testSearchCustomerBasedOnRegion_Positive(){
		CustomerProfile customer2 = CustomerProfile.builder().name("Jithendar")
				.phoneNumber("7778636363").emailId("Jith23@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList("SOUTH","MUSIC","REGULAR")).build();
		CustomerProfile customer3 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Thamz123@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList("NORTH","SPORTS","NEW")).build();
		List<CustomerProfile> regionList= List.of(customer2, customer3);
		when(customerProfileRepository.findAll()).thenReturn(regionList);

		List<CustomerProfileDTO> foundList = customerServiceImpl.searchCustomerBasedOnRegion(Region.NORTH);
		assertEquals(1, foundList.size());
	}

	@Test
	void testSearchCustomerBasedOnRegion_Negative(){
		List<CustomerProfile> regionList= List.of();
		when(customerProfileRepository.findAll()).thenReturn(regionList);

		assertThrows(ResourceNotFoundException.class, ()->{
			List<CustomerProfileDTO> foundList = customerServiceImpl.searchCustomerBasedOnRegion(Region.NORTH);
		});
	}

	@Test
	void testSearchCustomerBasedOnInterest_Positive(){
		CustomerProfile customer2 = CustomerProfile.builder().name("Jithendar")
				.phoneNumber("7778636363").emailId("Jith23@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList("NORTH","SPORTS","REGULAR")).build();
		CustomerProfile customer3 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Thamz123@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList("NORTH","MUSIC","NEW")).build();
		List<CustomerProfile> interestList= List.of(customer2, customer3);
		when(customerProfileRepository.findAll()).thenReturn(interestList);

		List<CustomerProfileDTO> foundList = customerServiceImpl.searchCustomerBasedOnInterest(Interest.SPORTS);
		assertEquals(1, foundList.size());
	}

	@Test
	void testSearchCustomerBasedOnInterest_Negative() {
		List<CustomerProfile> interestList = List.of();
		when(customerProfileRepository.findAll()).thenReturn(interestList);

		assertThrows(ResourceNotFoundException.class, () -> {
			List<CustomerProfileDTO> foundPhoneNumberList = customerServiceImpl.searchCustomerBasedOnInterest(Interest.SPORTS);
		});
	}

	@Test
	void testSearchCustomerBasedOnPurchasingHabit_Positive(){
		CustomerProfile customer2 = CustomerProfile.builder().name("Jithendar")
				.phoneNumber("7778636363").emailId("Jith23@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList("NORTH","SPORTS","REGULAR")).build();
		CustomerProfile customer3 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Thamz123@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList("NORTH","SPORTS","NEW")).build();
		List<CustomerProfile> habitList= List.of(customer2, customer3);
		when(customerProfileRepository.findAll()).thenReturn(habitList);

		List<CustomerProfileDTO> foundList = customerServiceImpl.searchCustomerBasedOnPurchasingHabit(PurchasingHabits.NEW);
		assertEquals(1, foundList.size());
	}

	@Test
	void testSearchCustomerBasedOnPurchasingHabit_Negative() {
		List<CustomerProfile> habitList = List.of();
		when(customerProfileRepository.findAll()).thenReturn(habitList);

		assertThrows(ResourceNotFoundException.class, () -> {
			List<CustomerProfileDTO> foundPhoneNumberList = customerServiceImpl.searchCustomerBasedOnPurchasingHabit(PurchasingHabits.REGULAR);
		});
	}

	@Test
	void testSearchCustomerBasedOnRegionAndPurchasingHabit_Positive(){
		CustomerProfile customer2 = CustomerProfile.builder().name("Jithendar")
				.phoneNumber("7778636363").emailId("Jith23@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList("NORTH","SPORTS","REGULAR")).build();
		CustomerProfile customer3 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Thamz123@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList("NORTH","SPORTS","NEW")).build();
		List<CustomerProfile> List= new ArrayList<>();
        List.add(customer2);
        List.add(customer3);
        when(customerProfileRepository.findAll()).thenReturn(List);

		List<CustomerProfileDTO> foundList = customerServiceImpl.searchCustomerBasedOnRegionAndPurchasingHabit(Region.NORTH, PurchasingHabits.NEW);
		assertEquals(1, foundList.size());
	}

	@Test
	void testSearchCustomerBasedOnRegionAndPurchasingHabit_Negative() {
		List<CustomerProfile> List = new ArrayList<>();
		when(customerProfileRepository.findAll()).thenReturn(List);

		assertThrows(ResourceNotFoundException.class, () -> {
			List<CustomerProfileDTO> foundPhoneNumberList = customerServiceImpl.searchCustomerBasedOnRegionAndPurchasingHabit(Region.NORTH, PurchasingHabits.NEW);
		});
	}

	@Test
	void testSearchCustomerBasedOnInterestAndPurchasingHabit_Positive(){
		CustomerProfile customer2 = CustomerProfile.builder().name("Jithendar")
				.phoneNumber("7778636363").emailId("Jith23@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList("NORTH","SPORTS","REGULAR")).build();
		CustomerProfile customer3 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Thamz123@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList("NORTH","SPORTS","NEW")).build();
		List<CustomerProfile> List= new ArrayList<>();
		List.add(customer2);
		List.add(customer3);
		when(customerProfileRepository.findAll()).thenReturn(List);

		List<CustomerProfileDTO> foundList = customerServiceImpl.searchCustomerBasedOnInterestAndPurchasingHabit(Interest.SPORTS, PurchasingHabits.NEW);
		assertEquals(1, foundList.size());
	}

	@Test
	void testSearchCustomerBasedOnInterestAndPurchasingHabit_Negative() {
		List<CustomerProfile> List = new ArrayList<>();
		when(customerProfileRepository.findAll()).thenReturn(List);

		assertThrows(ResourceNotFoundException.class, () -> {
			List<CustomerProfileDTO> foundPhoneNumberList = customerServiceImpl.searchCustomerBasedOnInterestAndPurchasingHabit(Interest.SPORTS, PurchasingHabits.NEW);
		});
	}

	@Test
	void testSearchCustomerBasedOnRegionAndInterestAndPurchasingHabit_Positive(){
		CustomerProfile customer2 = CustomerProfile.builder().name("Jithendar")
				.phoneNumber("7778636363").emailId("Jith23@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList("NORTH","SPORTS","REGULAR")).build();
		CustomerProfile customer3 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Thamz123@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList("NORTH","SPORTS","NEW")).build();
		List<CustomerProfile> List= new ArrayList<>();
		List.add(customer2);
		List.add(customer3);
		when(customerProfileRepository.findAll()).thenReturn(List);

		List<CustomerProfileDTO> foundList = customerServiceImpl.searchCustomerBasedOnRegionAndInterestAndPurchasingHabit(Region.NORTH,Interest.SPORTS, PurchasingHabits.NEW);
		assertEquals(1, foundList.size());
	}

	@Test
	void testSearchCustomerBasedOnRegionAndInterestAndPurchasingHabit_Negative() {
		List<CustomerProfile> List = new ArrayList<>();
		when(customerProfileRepository.findAll()).thenReturn(List);

		assertThrows(ResourceNotFoundException.class, () -> {
			List<CustomerProfileDTO> foundPhoneNumberList = customerServiceImpl.searchCustomerBasedOnRegionAndInterestAndPurchasingHabit(Region.NORTH,Interest.SPORTS, PurchasingHabits.NEW);
		});
	}

	@Test
	void testSearchCustomerBasedOnRegionAndInterest_Positive(){
		CustomerProfile customer2 = CustomerProfile.builder().name("Jithendar")
				.phoneNumber("7778636363").emailId("Jith23@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList("SOUTH","SPORTS","REGULAR")).build();
		CustomerProfile customer3 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Thamz123@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList("NORTH","SPORTS","NEW")).build();
		List<CustomerProfile> List= new ArrayList<>();
		List.add(customer2);
		List.add(customer3);
		when(customerProfileRepository.findAll()).thenReturn(List);

		List<CustomerProfileDTO> foundList = customerServiceImpl.searchCustomerBasedOnRegionAndInterest(Region.NORTH, Interest.SPORTS);
		assertEquals(1, foundList.size());
	}

	@Test
	void testSearchCustomerBasedOnRegionAndInterest_Negative() {
		List<CustomerProfile> List = new ArrayList<>();
		when(customerProfileRepository.findAll()).thenReturn(List);

		assertThrows(ResourceNotFoundException.class, () -> {
			List<CustomerProfileDTO> foundPhoneNumberList = customerServiceImpl.searchCustomerBasedOnRegionAndInterest(Region.NORTH, Interest.SPORTS);
		});
	}


	@Test
    void testUpdatePurchasingHabitWhenPurchaseHistoryLessThan3() {
        List<String> purchaseHistory = List.of("purchase1", "purchase2");
        customerProfiles.get(0).setPurchaseHistory(purchaseHistory);
        when(customerProfileRepository.findById(customerProfiles.get(0).getCustomerID())).thenReturn(Optional.of(customerProfiles.get(0)));
        customerServiceImpl.updatePurchasingHabit(customerProfiles.get(0).getCustomerID());
        assertEquals(PurchasingHabits.NEW, customerProfiles.get(0).getPurchasingHabits());
    }

    @Test
    void testUpdatePurchasingHabitWhenPurchaseHistoryMoreThan3() {
        List<String> purchaseHistory = List.of("purchase1", "purchase2", "purchase3", "purchase4");
        customerProfiles.get(0).setPurchaseHistory(purchaseHistory);
        when(customerProfileRepository.findById(customerProfiles.get(0).getCustomerID())).thenReturn(Optional.of(customerProfiles.get(0)));
        customerServiceImpl.updatePurchasingHabit(customerProfiles.get(0).getCustomerID());
        assertEquals(PurchasingHabits.SPARSE, customerProfiles.get(0).getPurchasingHabits());
    }

    @Test
    void testUpdatePurchasingHabitWhenPurchaseHistoryMoreThan10() {
        List<String> purchaseHistory = List.of("purchase1", "purchase2", "purchase3", "purchase4", "purchase5", "purchase6", "purchase7", "purchase8", "purchase9", "purchase10", "purchase11");
        customerProfiles.get(0).setPurchaseHistory(purchaseHistory);
        when(customerProfileRepository.findById(customerProfiles.get(0).getCustomerID())).thenReturn(Optional.of(customerProfiles.get(0)));
        customerServiceImpl.updatePurchasingHabit(customerProfiles.get(0).getCustomerID());
        assertEquals(PurchasingHabits.REGULAR, customerProfiles.get(0).getPurchasingHabits());
    }

    @Test
    void testUpdatePurchasingHabitWhenPurchaseHistory25OrMore() {
        List<String> purchaseHistory = List.of(
            "purchase1", "purchase2", "purchase3", "purchase4", "purchase5", "purchase6", "purchase7", "purchase8", "purchase9", "purchase10",
            "purchase11", "purchase12", "purchase13", "purchase14", "purchase15", "purchase16", "purchase17", "purchase18", "purchase19", "purchase20",
            "purchase21", "purchase22", "purchase23", "purchase24", "purchase25"
        );
        customerProfiles.getFirst().setPurchaseHistory(purchaseHistory);
        when(customerProfileRepository.findById(customerProfiles.get(0).getCustomerID())).thenReturn(Optional.of(customerProfiles.get(0)));
        customerServiceImpl.updatePurchasingHabit(customerProfiles.get(0).getCustomerID());
        assertEquals(PurchasingHabits.FREQUENT, customerProfiles.get(0).getPurchasingHabits());
    }
    
    @Test
    void testUpdatePurchasingHabit_Negative() {
    	
    	 when(customerProfileRepository.findById(customerProfiles.get(0).getCustomerID())).thenReturn(Optional.empty());
    	 assertThrows(ResourceNotFoundException.class, ()->{
    		 customerServiceImpl.updatePurchasingHabit(customerProfiles.get(0).getCustomerID());
    	 });
   
    }

    @Test
	void testAddToPurchaseHistory_Positive(){

		CustomerProfile existingCustomer = customerProfiles.get(0);
		existingCustomer.setPurchaseHistory(Arrays.asList("purchase1", "purchase2"));

		CustomerProfileDTO customerProfileDTO = new CustomerProfileDTO();
		customerProfileDTO.setCustomerID(1L);
		customerProfileDTO.setPurchaseHistory(Arrays.asList("purchase1", "purchase2", "purchase3"));

		when(customerProfileRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
		when(customerProfileMapper.mapToDTO(existingCustomer)).thenReturn(customerProfileDTO);

		CustomerProfileDTO result = customerServiceImpl.addToPurchaseHistory(1L, "purchase3");

		assertNotNull(result);
		assertEquals(3, result.getPurchaseHistory().size());
		assertTrue(result.getPurchaseHistory().contains("purchase3"));

		verify(customerProfileRepository, times(1)).findById(1L);
		verify(customerProfileRepository, times(1)).save(existingCustomer);
		verify(customerProfileMapper, times(1)).mapToDTO(existingCustomer);
	}

	@Test
	void testAddToPurchaseHistory_Negative(){
		when(customerProfileRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			customerServiceImpl.addToPurchaseHistory(1L, "purchase3");
		});
	}

	@Test
	void testAddMultiplePurchasesToPurchaseHistory_Positive() throws ResourceNotFoundException {
		CustomerProfile existingCustomer = customerProfiles.get(0);
		existingCustomer.setPurchaseHistory(Arrays.asList("purchase1", "purchase2"));

		CustomerProfileDTO customerProfileDTO = new CustomerProfileDTO();
		customerProfileDTO.setCustomerID(1L);
		customerProfileDTO.setPurchaseHistory(Arrays.asList("purchase1", "purchase2", "purchase3","purchase4"));

		when(customerProfileRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
		when(customerProfileMapper.mapToDTO(existingCustomer)).thenReturn(customerProfileDTO);

		CustomerProfileDTO result = customerServiceImpl.addMultiplePurchasesToPurchaseHistory(1L, Arrays.asList("purchase3", "purchase4"));

		assertNotNull(result);
		assertEquals(4, result.getPurchaseHistory().size());
		assertTrue(result.getPurchaseHistory().containsAll(Arrays.asList("purchase3", "purchase4")));

		verify(customerProfileRepository, times(1)).findById(1L);
		verify(customerProfileRepository, times(1)).save(existingCustomer);
		verify(customerProfileMapper, times(1)).mapToDTO(existingCustomer);
	}

	@Test
	void testAddMultiplePurchasesToPurchaseHistory_Negative() {
		when(customerProfileRepository.findById(1L)).thenReturn(Optional.empty());

		List<String> list = Arrays.asList("purchase3", "purchase4");
		assertThrows(ResourceNotFoundException.class, () -> {
			customerServiceImpl.addMultiplePurchasesToPurchaseHistory(1L, list);
		});

		verify(customerProfileRepository, times(1)).findById(1L);

	}




}
