package com.crm.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.crm.entities.CustomerProfile;

@DataJpaTest
@ActiveProfiles("test")
class CustomerProfileRepositoryTestCase {

	private CustomerProfile customerProfile;

	@Autowired
	private CustomerProfileRepository customerProfileRepository;

	@BeforeEach
	void setUp() throws Exception {
		customerProfile = CustomerProfile.builder().name("John Doe")
				.phoneNumber("9998887777").emailId("john.doe@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData("{\"segmentationData\": {\"Region\": \"NORTH\", \"Interest\": \"SPORTS\", \"PurchasingHabits\": \"NEW\"}}")
				.build();
	}

	@AfterEach
	void tearDown() throws Exception {
		customerProfileRepository.deleteAll();
		customerProfile = null;
	}

	@Test
	void testAddCustomerProfile_positive() {
		CustomerProfile savedCustomerProfile = customerProfileRepository.save(customerProfile);
		assertTrue(savedCustomerProfile.getCustomerID() > 0, "Customer ID should be generated");
	}

	@Test
	void testAddCustomerProfile_Negative() {
		assertThrows(Exception.class, () -> customerProfileRepository.save(null));
	}

	@Test
	void testFindCustomerProfileById_Positive() {
		CustomerProfile savedCustomerProfile = customerProfileRepository.save(customerProfile);
		Optional<CustomerProfile> optionalOfCustomerProfile = customerProfileRepository
				.findById(savedCustomerProfile.getCustomerID());
		assertTrue(optionalOfCustomerProfile.isPresent(), "Customer should be found by ID");
	}

	@Test
	void testFindCustomerProfileById_Negative() {
		Optional<CustomerProfile> optionalOfCustomerProfile = customerProfileRepository.findById(1L);
		assertTrue(optionalOfCustomerProfile.isEmpty(), "Customer with ID 1 should not exist");
	}

	@Test
	void testFindAllCustomerProfile_Positive() {
		customerProfileRepository.save(customerProfile);
		List<CustomerProfile> listOfCustomerProfile = customerProfileRepository.findAll();
		assertFalse(listOfCustomerProfile.isEmpty(), "List of customers should not be empty");
	}

	@Test
	void testFindAllCustomerProfile_Negative() {
		List<CustomerProfile> listOfCustomerProfile = customerProfileRepository.findAll();
		assertTrue(listOfCustomerProfile.isEmpty(), "List of customers should be empty");
	}

	@Test
	void testDeleteCustomerProfileById_Positive() {
		CustomerProfile savedCustomerProfile = customerProfileRepository.save(customerProfile);
		customerProfileRepository.deleteById(savedCustomerProfile.getCustomerID());
		Optional<CustomerProfile> optionalOfCustomerProfile = customerProfileRepository
				.findById(savedCustomerProfile.getCustomerID());
		assertTrue(optionalOfCustomerProfile.isEmpty(), "Customer should be deleted");
	}

	@Test
	void testDeleteCustomerProfileById_Negative() {
		assertThrows(Exception.class, () -> customerProfileRepository.deleteById(null));
	}


	@Test
	void testFindByEmail_Positive() {
		customerProfileRepository.save(customerProfile);
		Optional<CustomerProfile> optionalOfCustomerProfile = customerProfileRepository
				.findByEmail(customerProfile.getEmailId());
		assertTrue(optionalOfCustomerProfile.isPresent(), "Customer should be found by email");
	}

	@Test
	void testFindByEmail_Negative() {
		Optional<CustomerProfile> optionalOfCustomerProfile = customerProfileRepository
				.findByEmail("nonexistent@example.com");
		assertFalse(optionalOfCustomerProfile.isPresent(), "Customer should not be found by email");
	}

	@Test
	void testFindByContactNumber_Positive() {
		customerProfileRepository.save(customerProfile);
		Optional<CustomerProfile> optionalOfCustomerProfile = customerProfileRepository
				.findByContactNumber(customerProfile.getPhoneNumber());
		assertTrue(optionalOfCustomerProfile.isPresent(), "Customer should be found by phone number");
	}

	@Test
	void testFindByContactNumber_Negative() {
		Optional<CustomerProfile> optionalOfCustomerProfile = customerProfileRepository
				.findByContactNumber("0000000000");
		assertFalse(optionalOfCustomerProfile.isPresent(), "Customer should not be found by phone number");
	}

	@Test
	void testFindByName_Positive() {
		customerProfileRepository.save(customerProfile);
		Optional<CustomerProfile> customerProfiles = customerProfileRepository.findByNameContaining("Doe");
		assertFalse(customerProfiles.isEmpty(), "Customer should be found by name containing");
		assertTrue(customerProfiles.stream().anyMatch(c -> c.getName().equals("John Doe")), "Customer name should match");
	}

	@Test
	void testFindByName_Negative() {
		Optional<CustomerProfile> customerProfiles = customerProfileRepository.findByNameContaining("Smith");
		assertTrue(customerProfiles.isEmpty(), "Customer should not be found by name containing");
	}

	@Test
	void testFindAllByEmail_Positive() {
		customerProfileRepository.save(customerProfile);
		CustomerProfile customer2 = CustomerProfile.builder().name("SuvaLakshmi")
				.phoneNumber("7776665552").emailId("Suva2@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData("{\"segmentationData\": {\"Region\": \"NORTH\", \"Interest\": \"SPORTS\", \"PurchasingHabits\": \"NEW\"}}")
				.build();
		customerProfileRepository.save(customer2);
		CustomerProfile customer3 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("2223334445").emailId("Suva2@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData("{\"segmentationData\": {\"Region\": \"NORTH\", \"Interest\": \"SPORTS\", \"PurchasingHabits\": \"NEW\"}}")
				.build();
		customerProfileRepository.save(customer3);
		List<CustomerProfile> customerList = customerProfileRepository.findAllByEmailId("Suva2@example.com");
		assertEquals(2, customerList.size(), "Two customers should be found by email");
	}

	@Test
	void testFindAllByEmail_Negative() {
		List<CustomerProfile> customerList = customerProfileRepository.findAllByEmailId("Suva2@example.com");
		assertEquals(0, customerList.size(), "No customers should be found by email");
	}



	@Test
	void testFindAllPhoneNumber_Negative() {
		List<CustomerProfile> CustomerList = customerProfileRepository.findAllByPhoneNumber("7776665552");
		assertEquals(CustomerList.size(),0);
	}


	@Test
	void testFindAllByName_Positive() {
		customerProfileRepository.save(customerProfile);
		CustomerProfile customer2 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Suva2@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData("{\"segmentationData\": {\"Region\": \"NORTH\", \"Interest\": \"SPORTS\", \"PurchasingHabits\": \"NEW\"}}").build();
		customerProfileRepository.save(customer2);
		CustomerProfile customer3 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Suva2@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData("{\"segmentationData\": {\"Region\": \"NORTH\", \"Interest\": \"SPORTS\", \"PurchasingHabits\": \"NEW\"}}").build();
		customerProfileRepository.save(customer3);
		List<CustomerProfile> CustomerList = customerProfileRepository.findAllByNames("Thamizhini");
		assertEquals(CustomerList.size(),2);
	}

	@Test
	void testFindAllByName_Negative() {
		List<CustomerProfile> CustomerList = customerProfileRepository.findAllByNames("Thamizhini");
		assertEquals(CustomerList.size(),0);
	}





}
