package com.crm.service;

import com.crm.dto.CustomerProfileDTO;
import com.crm.entities.CustomerProfile;
import com.crm.enums.PurchasingHabits;
import com.crm.exception.ResourceNotFoundException;
import com.crm.mapper.CustomerProfileMapper;
import com.crm.repository.CustomerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerProfileRepository customerProfileRepository;
	@Autowired
	private CustomerProfileMapper customerProfileMapper;

	@Override
	public CustomerProfileDTO addCustomerProfile(CustomerProfileDTO customerProfileDTO) throws ResourceNotFoundException {
		if(customerProfileDTO == null){
			throw new ResourceNotFoundException("Enter valid Customer Profile Details");
		}
		CustomerProfile customerProfile = customerProfileMapper.mapToCustomer(customerProfileDTO);
		CustomerProfile savedCustomer = customerProfileRepository.save(customerProfile);
		return customerProfileMapper.mapToDTO(savedCustomer);
	}

	@Override
	public List<CustomerProfileDTO> retrieveAllProfiles() throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = (customerProfileRepository.findAll());
		if(customerProfiles.isEmpty()){
			throw new ResourceNotFoundException("No customers found");
		}
		return customerProfiles.stream().map(customerProfileMapper::mapToDTO).collect(Collectors.toList());
	}

	@Override
	public CustomerProfileDTO getCustomerById(Long customerId) throws ResourceNotFoundException {
		CustomerProfile customerProfile = customerProfileRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
		return customerProfileMapper.mapToDTO(customerProfile);
	}

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

	@Override
	public void deleteCustomer(Long customerId) throws ResourceNotFoundException {

		CustomerProfile existingCustomer = customerProfileRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
		customerProfileRepository.deleteById(existingCustomer.getCustomerID());

	}


	
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnEmailId(String email) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAllByEmailId(email);
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException("No customers found with the given email");
		}
		return customerProfiles.stream().map(customerProfileMapper::mapToDTO).collect(Collectors.toList());
	}
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnName(String name) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAllByNames(name);
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException("No customers found with the given name");
		}
		return customerProfiles.stream().map(customerProfileMapper::mapToDTO).collect(Collectors.toList());
	}
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnPhoneNumber(String phoneNumber)
			throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAllByPhoneNumber(phoneNumber);
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException("No customers found with the given phonenumber");
		}
		return customerProfiles.stream().map(customerProfileMapper::mapToDTO).collect(Collectors.toList());
	}

	@Override
	public PurchasingHabits displayCustomerPurchasingHabit(Long customerId) throws ResourceNotFoundException {
		CustomerProfile existingCustomer = customerProfileRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
		return existingCustomer.getPurchasingHabits();
	}

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
