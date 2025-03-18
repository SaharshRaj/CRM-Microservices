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
				.segmentationData(Arrays.asList()).build();
	}

	@AfterEach
	void tearDown() throws Exception {
		customerProfile = null;
	}

	@Test
	void testAddCustomerProfile_positive() {

		CustomerProfile savedCustomerProfile = customerProfileRepository.save(customerProfile);
		assertTrue(savedCustomerProfile.getCustomerID() > 0, "");
	}

	@Test
	void testAddCustomerProfile_Negative() {

		try {
			CustomerProfile savedCustomerProfile = customerProfileRepository.save(null);
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(true);
		}

	}

	@Test
	void testFindCustomerProfileById_Positive() {

		CustomerProfile savedCustomerProfile = customerProfileRepository.save(customerProfile);
		Optional<CustomerProfile> optionalOfCustomerProfile = customerProfileRepository
				.findById(savedCustomerProfile.getCustomerID());
		assertTrue(optionalOfCustomerProfile.isPresent());

	}

	@Test
	void testFindCustomerProfileById_Negative() {

		Optional<CustomerProfile> optionalOfCustomerProfile = customerProfileRepository.findById(1L);
		assertTrue(optionalOfCustomerProfile.isEmpty());

	}

	@Test
	void testFindAllCustomerProfile_Positive() {
		CustomerProfile savedCustomerProfile = customerProfileRepository.save(customerProfile);
		Iterable<CustomerProfile> listOfCustomerProfile = customerProfileRepository.findAll();
		assertTrue(listOfCustomerProfile.iterator().hasNext());
	}

	@Test
	void testFindAllCustomerProfile_Negative() {

		Iterable<CustomerProfile> listOfCustomerProfile = customerProfileRepository.findAll();
		assertFalse(listOfCustomerProfile.iterator().hasNext());

	}

	@Test
	void testDeleteCustomerProfileById_Positive() {

		CustomerProfile savedCustomerProfile = customerProfileRepository.save(customerProfile);
		customerProfileRepository.deleteById(savedCustomerProfile.getCustomerID());
		Optional<CustomerProfile> optionalOfCustomerProfile = customerProfileRepository.findById(1L);
		assertTrue(optionalOfCustomerProfile.isEmpty());
	}

	@Test
	void testDeleteCustomerProfileById_Negative() {
		try {
			customerProfileRepository.deleteById(null);
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	void testUpdateCustomerProfile() {

		CustomerProfile savedCustomerProfile = customerProfileRepository.save(customerProfile);
		Optional<CustomerProfile> optionalOfCustomerProfile = customerProfileRepository
				.findById(savedCustomerProfile.getCustomerID());
		assertTrue(optionalOfCustomerProfile.isPresent());
		savedCustomerProfile.setName("RomanReigns");
		assertEquals(savedCustomerProfile.getName(), "RomanReigns");

	}
	
	@Test
	void testFindByEmail_Positive(){
		  CustomerProfile savedCustomerProfile = customerProfileRepository.save(customerProfile);
	        Optional<CustomerProfile> optionalOfCustomerProfile = customerProfileRepository
	                .findByEmail(savedCustomerProfile.getEmailId());
	        assertTrue(optionalOfCustomerProfile.isPresent());
	}
	@Test
	void testFindByEmail_Negative(){
		Optional<CustomerProfile> optionalOfCustomerProfile = customerProfileRepository
				.findByEmail("john.doe@example.com");
		assertFalse(optionalOfCustomerProfile.isPresent());
	}
	
	@Test
	void testFindByContactNumber_Positive(){
		CustomerProfile savedCustomerProfile = customerProfileRepository.save(customerProfile);
		Optional<CustomerProfile> optionalOfCustomerProfile = customerProfileRepository
				.findByContactNumber(savedCustomerProfile.getPhoneNumber());
		assertTrue(optionalOfCustomerProfile.isPresent());
	}
	@Test
	void testFindByContactNumber_Negative(){
		Optional<CustomerProfile> optionalOfCustomerProfile = customerProfileRepository
				.findByContactNumber("9998887777");
		assertFalse(optionalOfCustomerProfile.isPresent());
	}
	
	@Test
	void testFindByName_Positive() {
		customerProfileRepository.save(customerProfile);
		Optional<CustomerProfile> customerProfiles = customerProfileRepository.findByNameContaining("Doe");
	    assertFalse(customerProfiles.isEmpty());
	    assertTrue(customerProfiles.stream().anyMatch(c -> c.getName().equals("John Doe")));
	}
	
	@Test
	void testFindByName_Negative() {
		
		Optional<CustomerProfile> customerProfiles = customerProfileRepository.findByNameContaining("Smith");

	    // Verify the results
	    assertTrue(customerProfiles.isEmpty());
	}
	
	@Test
	void testFindAllByEmail_Positive() {
		customerProfileRepository.save(customerProfile);
		CustomerProfile customer2 = CustomerProfile.builder().name("SuvaLakshmi")
				.phoneNumber("7776665552").emailId("Suva2@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList()).build();
		customerProfileRepository.save(customer2);
		CustomerProfile customer3 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("2223334445").emailId("Suva2@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList()).build();
		customerProfileRepository.save(customer3);
		List<CustomerProfile> CustomerList = customerProfileRepository.findAllByEmailId("Suva2@example.com");
		assertEquals(CustomerList.size(),2);
	}
	
	@Test
	void testFindAllByEmail_Negative() {
		List<CustomerProfile> CustomerList = customerProfileRepository.findAllByEmailId("Suva2@example.com");
		assertEquals(CustomerList.size(),0);
	}
	
	

	@Test
	void testFindAllByPhonrNumber_Positive() {
		customerProfileRepository.save(customerProfile);
		CustomerProfile customer2 = CustomerProfile.builder().name("SuvaLakshmi")
				.phoneNumber("7776665552").emailId("Suva2@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList()).build();
		customerProfileRepository.save(customer2);
		CustomerProfile customer3 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Suva2@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList()).build();
		customerProfileRepository.save(customer3);
		List<CustomerProfile> CustomerList = customerProfileRepository.findAllByPhoneNumber("7776665552");
		assertEquals(CustomerList.size(),2);
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
				.segmentationData(Arrays.asList()).build();
		customerProfileRepository.save(customer2);
		CustomerProfile customer3 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Suva2@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData(Arrays.asList()).build();
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
