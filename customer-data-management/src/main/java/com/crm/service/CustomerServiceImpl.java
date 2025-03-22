package com.crm.service;

import com.crm.dto.CustomerProfileDTO;
import com.crm.entities.CustomerProfile;
import com.crm.enums.Interest;
import com.crm.enums.PurchasingHabits;
import com.crm.enums.Region;
import com.crm.exception.EnumValueNotFoundException;
import com.crm.exception.ResourceNotFoundException;
import com.crm.mapper.CustomerProfileMapper;
import com.crm.repository.CustomerProfileRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service implementation for managing customer profiles.
 */
@Service
public class CustomerServiceImpl implements CustomerService {

	private final CustomerProfileRepository customerProfileRepository;
	private final CustomerProfileMapper customerProfileMapper;
	private final ObjectMapper objectMapper;

	private static final String PURCHASING_HABITS = "Purchasing Habit";
	private static final String SEGMENTATION_DATA = "Segmentation Data";

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
		List<CustomerProfileDTO> list = customerProfiles.stream()
				.filter(c -> getRegionFromSegmentation(c) == region && getPurchasingHabitsFromSegmentation(c) == purchasingHabits)
				.map(customerProfileMapper::toDTO)
				.collect(Collectors.toList());
		if(list.isEmpty()){
			throw new ResourceNotFoundException("There are no customers from this region with this purchasinghabit");
		}
		return list;
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
	public CustomerProfileDTO addToPurchaseHistory(Long customerId, String purchase) throws ResourceNotFoundException, JsonProcessingException {
		Map<String, String> jsonObject = objectMapper.readValue(purchase, Map.class);
		String purchaseHistory = jsonObject.get("purchaseHistory");
		CustomerProfile existingCustomer = customerProfileRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
		// Perform any necessary validation on the purchase string and purchasingHabits here.

		existingCustomer.getPurchaseHistory().add(purchaseHistory); // or purchase, if that is the intended behavior
		CustomerProfile updatedCustomer = customerProfileRepository.save(existingCustomer);

        return customerProfileMapper.toDTO(updatedCustomer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerProfileDTO addMultiplePurchasesToPurchaseHistory(Long customerId, String jsonBody) throws ResourceNotFoundException, JsonProcessingException {
		CustomerProfile existingCustomer = customerProfileRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

		Map<String, List<String>> jsonObject = objectMapper.readValue(jsonBody, Map.class);

		List<String> newPurchases = jsonObject.get("purchaseHistory");

		List<String> purchaseHistory = new ArrayList<>(existingCustomer.getPurchaseHistory());
		purchaseHistory.addAll(newPurchases);
		existingCustomer.setPurchaseHistory(purchaseHistory);
		CustomerProfile updatedCustomer = customerProfileRepository.save(existingCustomer);
		return customerProfileMapper.toDTO(updatedCustomer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerProfileDTO updatePurchasingHabit(Long customerId) throws ResourceNotFoundException, JsonProcessingException {
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


			String segmentationDataString = existingCustomer.getSegmentationData();

			if (segmentationDataString == null || segmentationDataString.isEmpty()) {
				// Handle the case where segmentationData is null or empty.
				Map<String, Map<String, String>> newSegmentationMap = new HashMap<>();
				Map<String, String> segmentationData = new HashMap<>();
				segmentationData.put("Interest", null);
				segmentationData.put("Region", null);
				segmentationData.put(PURCHASING_HABITS, newPurchasingHabit.name());
				newSegmentationMap.put(SEGMENTATION_DATA, segmentationData);

				existingCustomer.setSegmentationData(objectMapper.writeValueAsString(newSegmentationMap));
			} else {
				Map<String, Map<String, String>> segmentationMap = objectMapper.readValue(segmentationDataString, Map.class);

				Map<String, String> segmentationData = segmentationMap.get(SEGMENTATION_DATA);
				if (segmentationData != null) {
					segmentationData.put(PURCHASING_HABITS, newPurchasingHabit.name());
					existingCustomer.setSegmentationData(objectMapper.writeValueAsString(segmentationMap));
				} else {
					throw new ResourceNotFoundException("Segmentation data is missing or invalid.");
				}
			}

			CustomerProfile updatedCustomer = customerProfileRepository.save(existingCustomer);
			return customerProfileMapper.toDTO(updatedCustomer);

	}



	private Region getRegionFromSegmentation(CustomerProfile customerProfile) {
		return getEnumFromSegmentation(customerProfile, "Region", Region.class);
	}

	private Interest getInterestFromSegmentation(CustomerProfile customerProfile) {
		return getEnumFromSegmentation(customerProfile, "Interest", Interest.class);
	}

	private PurchasingHabits getPurchasingHabitsFromSegmentation(CustomerProfile customerProfile) {
		return getEnumFromSegmentation(customerProfile, PURCHASING_HABITS, PurchasingHabits.class);
	}

	public <T extends Enum<T>> T getEnumFromSegmentation(CustomerProfile customerProfile, String fieldName, Class<T> enumType) {
		if (customerProfile.getSegmentationData() == null || customerProfile.getSegmentationData().isEmpty()) {
			return null;
		}

		try {
			Map<String, Map<String, String>> segmentationMap = objectMapper.readValue(customerProfile.getSegmentationData(), Map.class);

			Map<String, String> segmentationData = segmentationMap.get(SEGMENTATION_DATA);
			if (segmentationData == null) {
				return null;
			}

			String enumValue = segmentationData.get(fieldName);
			if (enumValue != null) {
					return Enum.valueOf(enumType, enumValue);
			} else {
				throw new EnumValueNotFoundException("enum is null");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}