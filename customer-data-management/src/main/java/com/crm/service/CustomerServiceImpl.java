package com.crm.service;

import com.crm.dto.CustomerProfileDTO;
import com.crm.entities.CustomerProfile;
import com.crm.enums.Interest;
import com.crm.enums.PurchasingHabits;
import com.crm.enums.Region;
import com.crm.exception.ResourceNotFoundException;
import com.crm.mapper.CustomerProfileMapper;
import com.crm.repository.CustomerProfileRepository;
import com.crm.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing customer profiles.
 */
@Service
public class CustomerServiceImpl implements CustomerService {

	private final CustomerProfileRepository customerProfileRepository;
	private final CustomerProfileMapper customerProfileMapper;
	private final ObjectMapper objectMapper;

	@Autowired
	public CustomerServiceImpl(CustomerProfileRepository customerProfileRepository,
							   CustomerProfileMapper customerProfileMapper,
							   ObjectMapper objectMapper) {
		this.customerProfileRepository = customerProfileRepository;
		this.customerProfileMapper = customerProfileMapper;
		this.objectMapper = objectMapper;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnRegion(Region region) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAll();
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException("There are no Customers");
		}
		return customerProfiles.stream()
				.filter(c -> getRegionFromSegmentation(c) == region)
				.map(customerProfileMapper::toDTO)
				.collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnInterest(Interest interest) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAll();
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException("There are no Customers");
		}
		return customerProfiles.stream()
				.filter(c -> getInterestFromSegmentation(c) == interest)
				.map(customerProfileMapper::toDTO)
				.collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnPurchasingHabit(PurchasingHabits purchasingHabits) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAll();
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException("There are no Customers");
		}
		return customerProfiles.stream()
				.filter(c -> getPurchasingHabitsFromSegmentation(c) == purchasingHabits)
				.map(customerProfileMapper::toDTO)
				.collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnRegionAndInterest(Region region, Interest interest) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAll();
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException("There are no Customers");
		}
		return customerProfiles.stream()
				.filter(c -> getRegionFromSegmentation(c) == region && getInterestFromSegmentation(c) == interest)
				.map(customerProfileMapper::toDTO)
				.collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnRegionAndPurchasingHabit(Region region, PurchasingHabits purchasingHabits) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAll();
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException("There are no Customers");
		}
		return customerProfiles.stream()
				.filter(c -> getRegionFromSegmentation(c) == region && getPurchasingHabitsFromSegmentation(c) == purchasingHabits)
				.map(customerProfileMapper::toDTO)
				.collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnInterestAndPurchasingHabit(Interest interest, PurchasingHabits purchasingHabits) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAll();
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException("There are no Customers");
		}
		return customerProfiles.stream()
				.filter(c -> getInterestFromSegmentation(c) == interest && getPurchasingHabitsFromSegmentation(c) == purchasingHabits)
				.map(customerProfileMapper::toDTO)
				.collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerProfileDTO addCustomerProfile(CustomerProfileDTO customerProfileDTO) throws ResourceNotFoundException {
		if (customerProfileDTO == null) {
			throw new ResourceNotFoundException("Enter valid Customer Profile Details");
		}
		CustomerProfile customerProfile = customerProfileMapper.toEntity(customerProfileDTO);
		CustomerProfile savedCustomer = customerProfileRepository.save(customerProfile);
		return customerProfileMapper.toDTO(savedCustomer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> retrieveAllProfiles() throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAll();
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException("No customers found");
		}
		return customerProfiles.stream().map(customerProfileMapper::toDTO).collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerProfileDTO getCustomerById(Long customerId) throws ResourceNotFoundException {
		CustomerProfile customerProfile = customerProfileRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
		return customerProfileMapper.toDTO(customerProfile);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerProfileDTO updateCustomer(Long customerId, CustomerProfileDTO customerProfileDTO) throws ResourceNotFoundException {
		CustomerProfile existingCustomer = customerProfileRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
		CustomerProfile updatedCustomerProfile = customerProfileMapper.toEntity(customerProfileDTO);
		updatedCustomerProfile.setCustomerID(existingCustomer.getCustomerID());
		updatedCustomerProfile = customerProfileRepository.save(updatedCustomerProfile);
		return customerProfileMapper.toDTO(updatedCustomerProfile);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteCustomer(Long customerId) throws ResourceNotFoundException {
		CustomerProfile existingCustomer = customerProfileRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
		customerProfileRepository.deleteById(existingCustomer.getCustomerID());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnEmailId(String email) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAllByEmailId(email);
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException("No customers found with the given email");
		}
		return customerProfiles.stream().map(customerProfileMapper::toDTO).collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnName(String name) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAllByNames(name);
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException("No customers found with the given name");
		}
		return customerProfiles.stream().map(customerProfileMapper::toDTO).collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnPhoneNumber(String phoneNumber) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAllByPhoneNumber(phoneNumber);
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException("No customers found with the given phonenumber");
		}
		return customerProfiles.stream().map(customerProfileMapper::toDTO).collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnRegionAndInterestAndPurchasingHabit(Region region, Interest interest, PurchasingHabits purchasingHabits) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAll();
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException("There are no Customers");
		}
		return customerProfiles.stream()
				.filter(c -> getInterestFromSegmentation(c) == interest && getRegionFromSegmentation(c) == region && getPurchasingHabitsFromSegmentation(c) == purchasingHabits)
				.map(customerProfileMapper::toDTO)
				.collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerProfileDTO addToPurchaseHistory(Long customerId, String purchase) throws ResourceNotFoundException {
		CustomerProfile existingCustomer = customerProfileRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
		List<String> purchaseHistory = new ArrayList<>(existingCustomer.getPurchaseHistory());
		purchaseHistory.add(purchase);
		existingCustomer.setPurchaseHistory(purchaseHistory);
		CustomerProfile save = customerProfileRepository.save(existingCustomer);
		return customerProfileMapper.toDTO(save);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerProfileDTO addMultiplePurchasesToPurchaseHistory(Long customerId, List<String> purchase) throws ResourceNotFoundException {
		CustomerProfile existingCustomer = customerProfileRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
		List<String> purchaseHistory = new ArrayList<>(existingCustomer.getPurchaseHistory());
		purchaseHistory.addAll(purchase);
		existingCustomer.setPurchaseHistory(purchaseHistory);
		customerProfileRepository.save(existingCustomer);
		return customerProfileMapper.toDTO(existingCustomer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerProfileDTO updatePurchasingHabit(Long customerId) throws ResourceNotFoundException {
		CustomerProfile existingCustomer = customerProfileRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
		int numberOfPurchases = existingCustomer.getPurchaseHistory().size();
		PurchasingHabits newPurchasingHabit;

		if (numberOfPurchases <= 3) {
			newPurchasingHabit = PurchasingHabits.NEW;
		} else if (numberOfPurchases < 10) {
			newPurchasingHabit = PurchasingHabits.SPARSE;
		} else if (numberOfPurchases < 25) {
			newPurchasingHabit = PurchasingHabits.REGULAR;
		} else {
			newPurchasingHabit = PurchasingHabits.FREQUENT;
		}

		try {
			JsonNode jsonNode = objectMapper.readTree(existingCustomer.getSegmentationData()).get("segmentationData");
			if (jsonNode != null) {
				((com.fasterxml.jackson.databind.node.ObjectNode) jsonNode).put("PurchasingHabits", newPurchasingHabit.name());
				existingCustomer.setSegmentationData(objectMapper.writeValueAsString(objectMapper.createObjectNode().set("segmentationData", jsonNode)));
				customerProfileRepository.save(existingCustomer);
				return customerProfileMapper.toDTO(existingCustomer);
			} else {
				throw new ResourceNotFoundException("Segmentation data is missing or invalid.");
			}
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Failed to update purchasing habit in segmentation data.", e);
		}
	}

	private Region getRegionFromSegmentation(CustomerProfile customerProfile) {
		return getEnumFromSegmentation(customerProfile, "Region", Region.class);
	}

	private Interest getInterestFromSegmentation(CustomerProfile customerProfile) {
		return getEnumFromSegmentation(customerProfile, "Interest", Interest.class);
	}

	private PurchasingHabits getPurchasingHabitsFromSegmentation(CustomerProfile customerProfile) {
		return getEnumFromSegmentation(customerProfile, "Purchasing Habits", PurchasingHabits.class);
	}

	private <T extends Enum<T>> T getEnumFromSegmentation(CustomerProfile customerProfile, String fieldName, Class<T> enumType) {
		if (customerProfile.getSegmentationData() == null || customerProfile.getSegmentationData().isEmpty()) {
			return null;
		}

		try {
			JsonNode segmentationNode = objectMapper.readTree(customerProfile.getSegmentationData()).get("segmentationData");
			if (segmentationNode == null) {
				return null;
			}

			if (segmentationNode.has(fieldName)) {
				String enumValue = segmentationNode.get(fieldName).asText();
				try {
					return Enum.valueOf(enumType, enumValue);
				} catch (IllegalArgumentException e) {
					return null;
				}

			} else {
				return null;
			}
		} catch (JsonProcessingException e) {
			return null;
		}
	}
}