package com.crm.service;

import com.crm.dto.CustomerProfileDTO;
import com.crm.entities.CustomerProfile;
import com.crm.enums.Interest;
import com.crm.enums.PurchasingHabits;
import com.crm.enums.Region;
import com.crm.exception.ResourceNotFoundException;
import com.crm.mapper.CustomerProfileMapper;
import com.crm.repository.CustomerProfileRepository;
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


	private CustomerProfileRepository customerProfileRepository;


	private CustomerProfileMapper customerProfileMapper;

	@Autowired
	public CustomerServiceImpl(CustomerProfileRepository customerProfileRepository, CustomerProfileMapper customerProfileMapper){
		this.customerProfileRepository = customerProfileRepository;
		this.customerProfileMapper = customerProfileMapper;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnRegion(Region region) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAll();
		if(customerProfiles.isEmpty()){
			throw new ResourceNotFoundException("There are no Customers");
		}
		List<CustomerProfileDTO> list = customerProfiles.stream().filter(c-> c.getRegion() == region).map(customerProfileMapper::mapToDTO).toList();
		if(list.isEmpty()){
			throw new ResourceNotFoundException("There are no Customers");
		}
		return list;	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnInterest(Interest interest) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAll();
		if(customerProfiles.isEmpty()){
			throw new ResourceNotFoundException("There are no Customers");
		}
		List<CustomerProfileDTO> list = customerProfiles.stream().filter(c-> c.getInterest() == interest).map(customerProfileMapper::mapToDTO).toList();
		if(list.isEmpty()){
			throw new ResourceNotFoundException("There are no Customers");
		}
		return list;	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnPurchasingHabit(PurchasingHabits purchasingHabits) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAll();
		if(customerProfiles.isEmpty()){
			throw new ResourceNotFoundException("There are no Customers");
		}
		List<CustomerProfileDTO> list = customerProfiles.stream().filter(c-> c.getPurchasingHabits() == purchasingHabits).map(customerProfileMapper::mapToDTO).toList();
		if(list.isEmpty()){
			throw new ResourceNotFoundException("There are no Customers");
		}
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnRegionAndInterest(Region region, Interest interest) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAll();
		if(customerProfiles.isEmpty()){
			throw new ResourceNotFoundException("There are no Customers");
		}
		List<CustomerProfileDTO> list = customerProfiles.stream()
				.filter(c-> (c.getRegion() == region) && (c.getInterest() == interest))
				.map(customerProfileMapper::mapToDTO)
				.toList();
		if(list.isEmpty()){
			throw new ResourceNotFoundException("There are no Customers");
		}
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnRegionAndPurchasingHabit(Region region, PurchasingHabits purchasingHabits) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAll();
		if(customerProfiles.isEmpty()){
			throw new ResourceNotFoundException("There are no Customers");
		}
		List<CustomerProfileDTO> list = customerProfiles.stream()
				.filter(c-> (c.getRegion() == region) && (c.getPurchasingHabits() == purchasingHabits))
				.map(customerProfileMapper::mapToDTO)
				.toList();
		if(list.isEmpty()){
			throw new ResourceNotFoundException("There are no Customers");
		}
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnInterestAndPurchasingHabit(Interest interest, PurchasingHabits purchasingHabits) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAll();
		if(customerProfiles.isEmpty()){
			throw new ResourceNotFoundException("There are no Customers");
		}
		List<CustomerProfileDTO> list = customerProfiles.stream()
				.filter(c-> (c.getInterest() == interest) && (c.getPurchasingHabits() == purchasingHabits))
				.map(customerProfileMapper::mapToDTO)
				.toList();
		if(list.isEmpty()){
			throw new ResourceNotFoundException("There are no Customers");
		}
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerProfileDTO addCustomerProfile(CustomerProfileDTO customerProfileDTO) throws ResourceNotFoundException {
		if(customerProfileDTO == null){
			throw new ResourceNotFoundException("Enter valid Customer Profile Details");
		}
		CustomerProfile customerProfile = customerProfileMapper.mapToCustomer(customerProfileDTO);
		CustomerProfile savedCustomer = customerProfileRepository.save(customerProfile);
		return customerProfileMapper.mapToDTO(savedCustomer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> retrieveAllProfiles() throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = (customerProfileRepository.findAll());
		if(customerProfiles.isEmpty()){
			throw new ResourceNotFoundException("No customers found");
		}
		return customerProfiles.stream().map(customerProfileMapper::mapToDTO).collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerProfileDTO getCustomerById(Long customerId) throws ResourceNotFoundException {
		CustomerProfile customerProfile = customerProfileRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
		return customerProfileMapper.mapToDTO(customerProfile);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerProfileDTO updateCustomer(Long customerId, CustomerProfileDTO customerProfileDTO)
			throws ResourceNotFoundException {

		CustomerProfile existingCustomer = customerProfileRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
		existingCustomer.setName(customerProfileDTO.getName());
		existingCustomer.setPhoneNumber(customerProfileDTO.getPhoneNumber());
		existingCustomer.setEmailId(customerProfileDTO.getEmailId());
		existingCustomer.setSegmentationData(customerProfileDTO.getSegmentationData());
		existingCustomer.setPurchaseHistory(customerProfileDTO.getPurchaseHistory());
		CustomerProfile updatedCustomerProfile = customerProfileRepository.save(existingCustomer);
		return customerProfileMapper.mapToDTO(updatedCustomerProfile);

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
		return customerProfiles.stream().map(customerProfileMapper::mapToDTO).collect(Collectors.toList());
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
		return customerProfiles.stream().map(customerProfileMapper::mapToDTO).collect(Collectors.toList());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnPhoneNumber(String phoneNumber)
			throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAllByPhoneNumber(phoneNumber);
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException("No customers found with the given phonenumber");
		}
		return customerProfiles.stream().map(customerProfileMapper::mapToDTO).collect(Collectors.toList());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnRegionAndInterestAndPurchasingHabit(Region region, Interest interest, PurchasingHabits purchasingHabits) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAll();
		if(customerProfiles.isEmpty()){
			throw new ResourceNotFoundException("There are no Customers");
		}
		List<CustomerProfileDTO> list = customerProfiles.stream()
				.filter(c-> (c.getInterest() == interest) && (c.getRegion() == region) && (c.getPurchasingHabits() == purchasingHabits))
				.map(customerProfileMapper::mapToDTO).toList();
		if(list.isEmpty()){
			throw new ResourceNotFoundException("There are no Customers");
		}
		return list;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerProfileDTO addToPurchaseHistory( Long customerId, String purchase) throws ResourceNotFoundException {
		CustomerProfile existingCustomer = customerProfileRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
		List<String> purchaseHistory = new ArrayList<>(existingCustomer.getPurchaseHistory());
		purchaseHistory.add(purchase);
		existingCustomer.setPurchaseHistory(purchaseHistory);
		customerProfileRepository.save(existingCustomer);
		return customerProfileMapper.mapToDTO(existingCustomer);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerProfileDTO addMultiplePurchasesToPurchaseHistory ( Long customerId, List<String> purchase) throws ResourceNotFoundException {
		CustomerProfile existingCustomer = customerProfileRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
		List<String> purchaseHistory = new ArrayList<>(existingCustomer.getPurchaseHistory());
		purchaseHistory.addAll(purchase);
		existingCustomer.setPurchaseHistory(purchaseHistory);
		customerProfileRepository.save(existingCustomer);
		return customerProfileMapper.mapToDTO(existingCustomer);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerProfileDTO updatePurchasingHabit(Long customerId) throws ResourceNotFoundException {
		CustomerProfile existingCustomer = customerProfileRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
		int numberOfPurchases = existingCustomer.getPurchaseHistory().size();
		if (numberOfPurchases <= 3) {
			existingCustomer.setPurchasingHabits(PurchasingHabits.NEW);
		} else if (numberOfPurchases < 10) {
			existingCustomer.setPurchasingHabits(PurchasingHabits.SPARSE);
		} else if (numberOfPurchases < 25) {
			existingCustomer.setPurchasingHabits(PurchasingHabits.REGULAR);
		} else {
			existingCustomer.setPurchasingHabits(PurchasingHabits.FREQUENT);
		}
		customerProfileRepository.save(existingCustomer);
		return customerProfileMapper.mapToDTO(existingCustomer);
	}

}
