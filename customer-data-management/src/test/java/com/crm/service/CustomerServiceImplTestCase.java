package com.crm.service;

import com.crm.dto.CustomerProfileDTO;
import com.crm.entities.CustomerProfile;
import com.crm.enums.Interest;
import com.crm.enums.PurchasingHabits;
import com.crm.enums.Region;
import com.crm.exception.ResourceNotFoundException;
import com.crm.mapper.CustomerProfileMapper;
import com.crm.repository.CustomerProfileRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@Slf4j
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTestCase {

	@Mock
	private CustomerProfileRepository customerProfileRepository;

	@Mock
	private CustomerProfileMapper customerProfileMapper;

	@Mock
	private ObjectMapper objectMapper;


	@InjectMocks
	private CustomerServiceImpl customerServiceImpl;

	private List<CustomerProfileDTO> customerProfilesDTOs;
	private List<CustomerProfile> customerProfiles;

	@BeforeEach
	void setUp() {
		CustomerProfile customerProfile1 = CustomerProfile.builder()
				.customerID(1L)
				.name("John Doe")
				.emailId("john@example.com")
				.phoneNumber("1234567890")
				.segmentationData("{\"segmentationData\": {\"Region\": \"NORTH\", \"Interest\": \"SPORTS\", \"PurchasingHabits\": \"NEW\"}}")
				.build();

		CustomerProfile customerProfile2 = CustomerProfile.builder()
				.customerID(2L)
				.name("Jane Doe")
				.emailId("jane@example.com")
				.phoneNumber("0987654321")
				.segmentationData("{\"segmentationData\": {\"Region\": \"NORTH\", \"Interest\": \"SPORTS\", \"PurchasingHabits\": \"NEW\"}}")
				.build();

		customerProfiles = Arrays.asList(customerProfile1, customerProfile2);

		customerProfilesDTOs = Arrays.asList(
				CustomerProfileDTO.builder()
						.customerID(1L)
						.name("John Doe")
						.emailId("john@example.com")
						.phoneNumber("1234567890")
						.segmentationData(
								Map.of("Region","NORTH", "Interest","SPORT", "Purchasing Habits","NEW")
						)
						.build(),
				CustomerProfileDTO.builder()
						.customerID(2L)
						.name("Jane Doe")
						.emailId("jane@example.com")
						.phoneNumber("0987654321")
						.segmentationData(Map.of("Region","SOUTH", "Interest","MUSIC", "Purchasing Habits","REGULAR"))
						.build()
		);


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
		assertThrows(ResourceNotFoundException.class, () -> customerServiceImpl.retrieveAllProfiles());
		verify(customerProfileRepository, times(1)).findAll();
	}

	@Test
	void testAddCustomer_Profile() {
		CustomerProfile customerProfile = customerProfiles.getFirst();
		CustomerProfileDTO customerProfileDTO = customerProfilesDTOs.getFirst();

		when(customerProfileMapper.toEntity(customerProfileDTO)).thenReturn(customerProfile);
		when(customerProfileRepository.save(customerProfile)).thenReturn(customerProfile);

		customerServiceImpl.addCustomerProfile(customerProfileDTO);
		verify(customerProfileRepository, times(1)).save(customerProfile);
	}

	@Test
	void testAddCustomerProfile_Negative() {
		assertThrows(ResourceNotFoundException.class, () -> customerServiceImpl.addCustomerProfile(null));
	}

	@Test
	void testGetCustomerById_Positive() {
		when(customerProfileRepository.findById(1L)).thenReturn(Optional.of(customerProfiles.get(0)));
		when(customerProfileMapper.toDTO(customerProfiles.getFirst())).thenReturn(customerProfilesDTOs.getFirst());
		CustomerProfileDTO result = customerServiceImpl.getCustomerById(1L);
		assertEquals(customerProfilesDTOs.get(0), result);
		verify(customerProfileRepository, times(1)).findById(1L);
	}

	@Test
	void testGetCustomerById_Negative() {
		when(customerProfileRepository.findById(1L)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> customerServiceImpl.getCustomerById(1L));
	}

	@Test
	void testUpdateCustomer_Positive() throws ResourceNotFoundException {
		CustomerProfileDTO customerDTO = CustomerProfileDTO.builder()
				.customerID(1L)
				.name("John Doe")
				.emailId("john@example.com")
				.phoneNumber("1234567890")
				.segmentationData(Map.of("Region","NORTH", "Interest","SPORT", "Purchasing Habits","NEW"))
				.build();

		when(customerProfileMapper.toDTO(any(CustomerProfile.class))).thenAnswer(invocation -> {
			CustomerProfile customer = invocation.getArgument(0);
			return customerProfilesDTOs.stream().filter(dto -> dto.getCustomerID().equals(customer.getCustomerID())).findFirst().orElse(null);
		});

		when(customerProfileMapper.toEntity(any(CustomerProfileDTO.class))).thenAnswer(invocation -> {
			CustomerProfileDTO dto = invocation.getArgument(0);
			return customerProfiles.stream().filter(customer -> customer.getCustomerID().equals(dto.getCustomerID())).findFirst().orElse(null);
		});


		when(customerProfileRepository.findById(1L)).thenReturn(Optional.of(customerProfiles.get(0)));
		when(customerProfileRepository.save(any(CustomerProfile.class))).thenReturn(customerProfiles.get(0));

		CustomerProfileDTO result = customerServiceImpl.updateCustomer(1L, customerDTO);
		assertEquals(customerDTO, result);
	}

	@Test
	void testUpdateCustomer_Negative() throws ResourceNotFoundException {
		CustomerProfileDTO customerDTO = CustomerProfileDTO.builder()
				.customerID(1L)
				.name("John Doe")
				.emailId("john@example.com")
				.phoneNumber("1234567890")
				.segmentationData(Map.of("Region","NORTH", "Interest","SPORTS", "Purchasing Habits","NEW"))
				.build();
		when(customerProfileRepository.findById(1L)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> customerServiceImpl.updateCustomer(1L, customerDTO));
		verify(customerProfileRepository, never()).save(any(CustomerProfile.class));
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
		assertThrows(ResourceNotFoundException.class, () -> customerServiceImpl.deleteCustomer(1L));
	}

	@Test
	void testSearchCustomerBasedOnEmailId_Positive() {
		CustomerProfile customer2 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Thamz123@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData("{\"segmentationData\": {\"Region\": \"NORTH\", \"Interest\": \"SPORTS\", \"PurchasingHabits\": \"NEW\"}}")
				.build();
		CustomerProfile customer3 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Thamz123@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData("{\"segmentationData\": {\"Region\": \"NORTH\", \"Interest\": \"SPORTS\", \"PurchasingHabits\": \"NEW\"}}")
				.build();
		List<CustomerProfile> emailList = List.of(customer2, customer3);
		when(customerProfileRepository.findAllByEmailId("Thamz123@example.com")).thenReturn(emailList);
		List<CustomerProfileDTO> foundEmailList = customerServiceImpl.searchCustomerBasedOnEmailId("Thamz123@example.com");
		assertEquals(emailList.size(), foundEmailList.size());
	}

	@Test
	void testSearchCustomerBasedOnEmailId_Negative() {
		List<CustomerProfile> emailList = List.of();
		when(customerProfileRepository.findAllByEmailId("Thamz123@example.com")).thenReturn(emailList);
		assertThrows(ResourceNotFoundException.class, () -> customerServiceImpl.searchCustomerBasedOnEmailId("Thamz123@example.com"));
	}

	@Test
	void testSearchCustomerBasedOnName_Positive() {
		CustomerProfile customer2 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Thamz123@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData("{\"segmentationData\": {\"Region\": \"NORTH\", \"Interest\": \"SPORTS\", \"PurchasingHabits\": \"NEW\"}}")
				.build();
		CustomerProfile customer3 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Thamz123@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData("{\"segmentationData\": {\"Region\": \"NORTH\", \"Interest\": \"SPORTS\", \"PurchasingHabits\": \"NEW\"}}")
				.build();
		List<CustomerProfile> nameList = List.of(customer2, customer3);
		when(customerProfileRepository.findAllByNames("Thamizhini")).thenReturn(nameList);

		List<CustomerProfileDTO> foundNameList = customerServiceImpl.searchCustomerBasedOnName("Thamizhini");
		assertEquals(nameList.size(), foundNameList.size());
	}

	@Test
	void testSearchCustomerBasedOnName_Negative() {
		List<CustomerProfile> nameList = List.of();
		when(customerProfileRepository.findAllByNames("Thamizhini")).thenReturn(nameList);
		assertThrows(ResourceNotFoundException.class, () -> customerServiceImpl.searchCustomerBasedOnName("Thamizhini"));
	}

	@Test
	void testSearchCustomerBasedOnPhoneNumber_Positive() {
		CustomerProfile customer2 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Thamz123@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData("{\"segmentationData\": {\"Region\": \"NORTH\", \"Interest\": \"SPORTS\", \"PurchasingHabits\": \"NEW\"}}")
				.build();
		CustomerProfile customer3 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Thamz123@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData("{\"segmentationData\": {\"Region\": \"NORTH\", \"Interest\": \"SPORTS\", \"PurchasingHabits\": \"NEW\"}}")
				.build();
		List<CustomerProfile> phoneNumberList = List.of(customer2, customer3);
		when(customerProfileRepository.findAllByPhoneNumber("7776665552")).thenReturn(phoneNumberList);

		List<CustomerProfileDTO> foundPhoneNumberList = customerServiceImpl.searchCustomerBasedOnPhoneNumber("7776665552");
		assertEquals(phoneNumberList.size(), foundPhoneNumberList.size());
	}

	@Test
	void testSearchCustomerBasedOnPhoneNumber_Negative() {
		List<CustomerProfile> phoneNumberList = List.of();
		when(customerProfileRepository.findAllByPhoneNumber("7776665552")).thenReturn(phoneNumberList);
		assertThrows(ResourceNotFoundException.class, () -> customerServiceImpl.searchCustomerBasedOnPhoneNumber("7776665552"));
	}



	@Test
	void testSearchCustomerBasedOnRegion_Negative() {
		when(customerProfileRepository.findAll()).thenReturn(List.of());
		assertThrows(ResourceNotFoundException.class, () -> customerServiceImpl.searchCustomerBasedOnRegion(Region.NORTH));
	}



	@Test
	void testSearchCustomerBasedOnInterest_Negative() {
		when(customerProfileRepository.findAll()).thenReturn(List.of());
		assertThrows(ResourceNotFoundException.class, () -> customerServiceImpl.searchCustomerBasedOnInterest(Interest.SPORTS));
	}

	@Test
	void testSearchCustomerBasedOnPurchasingHabit_Negative() {
		when(customerProfileRepository.findAll()).thenReturn(List.of());
		assertThrows(ResourceNotFoundException.class, () -> customerServiceImpl.searchCustomerBasedOnPurchasingHabit(PurchasingHabits.REGULAR));
	}



	@Test
	void testSearchCustomerBasedOnRegionAndPurchasingHabit_Negative() {
		when(customerProfileRepository.findAll()).thenReturn(List.of());
		assertThrows(ResourceNotFoundException.class, () -> customerServiceImpl.searchCustomerBasedOnRegionAndPurchasingHabit(Region.NORTH, PurchasingHabits.NEW));
	}



	@Test
	void testSearchCustomerBasedOnInterestAndPurchasingHabit_Negative() {
		when(customerProfileRepository.findAll()).thenReturn(List.of());
		assertThrows(ResourceNotFoundException.class, () -> customerServiceImpl.searchCustomerBasedOnInterestAndPurchasingHabit(Interest.SPORTS, PurchasingHabits.NEW));
	}



	@Test
	void testSearchCustomerBasedOnRegionAndInterestAndPurchasingHabit_Negative() {
		when(customerProfileRepository.findAll()).thenReturn(List.of());
		assertThrows(ResourceNotFoundException.class, () -> customerServiceImpl.searchCustomerBasedOnRegionAndInterestAndPurchasingHabit(Region.NORTH, Interest.SPORTS, PurchasingHabits.NEW));
	}


	@Test
	void testSearchCustomerBasedOnRegionAndInterest_Negative() {
		when(customerProfileRepository.findAll()).thenReturn(List.of());
		assertThrows(ResourceNotFoundException.class, () -> customerServiceImpl.searchCustomerBasedOnRegionAndInterest(Region.NORTH, Interest.SPORTS));
	}


	@Test
	void testUpdatePurchasingHabit_Negative() {
		when(customerProfileRepository.findById(customerProfiles.get(0).getCustomerID())).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> customerServiceImpl.updatePurchasingHabit(customerProfiles.get(0).getCustomerID()));
	}

	@Test
	void testAddToPurchaseHistory_Positive() throws JsonProcessingException {
		CustomerProfile existingCustomer = customerProfiles.get(0);
		existingCustomer.setPurchaseHistory(new ArrayList<>(Arrays.asList("purchase1", "purchase2")));

		CustomerProfile updatedCustomer = customerProfiles.get(0);
		updatedCustomer.setPurchaseHistory(new ArrayList<>(Arrays.asList("purchase1", "purchase2", "purchase3")));

		CustomerProfileDTO customerProfileDTO = customerProfilesDTOs.get(0);
		customerProfileDTO.setPurchaseHistory(Arrays.asList("purchase1", "purchase2", "purchase3"));

		Map<String, String> purchase = new HashMap<>();
		purchase.put("purchaseHistory", "purchase3");

		// Correct the JSON string
		String json = "{\"purchaseHistory\":\"purchase3\"}";

		// Mock the ObjectMapper methods
		when(objectMapper.readValue(json, Map.class)).thenReturn(purchase);
		when(customerProfileRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
		when(customerProfileRepository.save(existingCustomer)).thenReturn(updatedCustomer);
		when(customerProfileMapper.toDTO(updatedCustomer)).thenReturn(customerProfileDTO);

		CustomerProfileDTO result = null;

		try {
			result = customerServiceImpl.addToPurchaseHistory(1L, json);
		} catch (JsonProcessingException e) {
			assertTrue(false);
		}

		assertNotNull(result);
		assertEquals(3, result.getPurchaseHistory().size());
		assertTrue(result.getPurchaseHistory().contains("purchase3"));

		verify(customerProfileRepository, times(1)).findById(1L);
		verify(customerProfileRepository, times(1)).save(existingCustomer);
	}

	@Test
	void testAddToPurchaseHistory_Negative() throws JsonProcessingException {
		Map<String, String> purchase = new HashMap<>();
		purchase.put("purchaseHistory","newPurchase");
		when(customerProfileRepository.findById(1L)).thenReturn(Optional.empty());
		when(objectMapper.readValue("{\"purchaseHistory\":\"newPurchase\"}",Map.class)).thenReturn(purchase);
		assertThrows(ResourceNotFoundException.class, () -> customerServiceImpl.addToPurchaseHistory(1L, "{\"purchaseHistory\":\"newPurchase\"}"));
	}

	@Test
	void testAddMultiplePurchasesToPurchaseHistory_Positive() throws ResourceNotFoundException, JsonProcessingException {
		CustomerProfile existingCustomer = customerProfiles.get(0);
		List<String> existingPurchases = Arrays.asList("purchase1", "purchase2");
		existingCustomer.setPurchaseHistory(existingPurchases);

		CustomerProfileDTO customerProfileDTO = customerProfilesDTOs.get(0);
		List<String> fullPurchaseList = new ArrayList<>(existingPurchases);
		fullPurchaseList.addAll(Arrays.asList("newPurchase1", "newPurchase2", "newPurchase3"));
		CustomerProfile updatedCustomer = existingCustomer;
		updatedCustomer.setPurchaseHistory(fullPurchaseList);

		customerProfileDTO.setPurchaseHistory(fullPurchaseList);

		Map<String, List<String>> purchase = new HashMap<>();
		List<String> purchaseList = Arrays.asList("newPurchase1","newPurchase2","newPurchase3");
		purchase.put("purchaseHistory",purchaseList);
		when(customerProfileRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
		when(customerProfileMapper.toDTO(existingCustomer)).thenReturn(customerProfileDTO);
		String json = objectMapper.writeValueAsString(purchase);
		when(objectMapper.writeValueAsString(purchase)).thenReturn(json);
		when(objectMapper.readValue(json, Map.class)).thenReturn(purchase);
		when(customerProfileRepository.save(any(CustomerProfile.class))).thenReturn(updatedCustomer);
		CustomerProfileDTO result = customerServiceImpl.addMultiplePurchasesToPurchaseHistory(1L, objectMapper.writeValueAsString(purchase));
		assertNotNull(result);
		assertEquals(5, result.getPurchaseHistory().size());
		assertTrue(result.getPurchaseHistory().containsAll(fullPurchaseList));

		verify(customerProfileRepository, times(1)).findById(1L);
		verify(customerProfileRepository, times(1)).save(existingCustomer);
	}

	@Test
	void testAddMultiplePurchasesToPurchaseHistory_Negative() {
		when(customerProfileRepository.findById(1L)).thenReturn(Optional.empty());

		List<String> list = Arrays.asList("purchase3", "purchase4");
		assertThrows(ResourceNotFoundException.class, () -> customerServiceImpl.addMultiplePurchasesToPurchaseHistory(1L, "{\n" +
				"    \"purchaseHstory\" : [\"newPurchase1\", \"newPurchase2\", \"newPurchase3\"]\n" +
				"}"));

		verify(customerProfileRepository, times(1)).findById(1L);
	}
}