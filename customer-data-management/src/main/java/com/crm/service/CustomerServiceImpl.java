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
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerProfileRepository customerProfileRepository;
	@Autowired
	private CustomerProfileMapper customerProfileMapper;

	@Override
	public List<CustomerProfileDTO> retrieveAllProfiles() throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = (customerProfileRepository.findAll());
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
	public List<CustomerProfileDTO> searchCustomers(String keyword) throws ResourceNotFoundException {
		if (isEmail(keyword)) {
			return searchCustomerBasedOnEmailId(keyword);
		} else if (isContactNumber(keyword)) {
			return searchCustomerBasedOnPhoneNumber(keyword);
		} else if (isName(keyword)) {
			return searchCustomerBasedOnName(keyword);
		} else {
			throw new ResourceNotFoundException("No customers found with the given keyword");
		}
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
			throw new ResourceNotFoundException("No customers found with the given phone number");
		}
		return customerProfiles.stream().map(customerProfileMapper::mapToDTO).collect(Collectors.toList());
	}

	private boolean isEmail(String keyword) {
		String emailRegex = "[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		Pattern pattern = Pattern.compile(emailRegex);
		return pattern.matcher(keyword).matches();
	}

	private boolean isContactNumber(String keyword) {
		String contactNumberRegex = "^[0-9]{10}$";
		Pattern pattern = Pattern.compile(contactNumberRegex);
		return pattern.matcher(keyword).matches();
	}

	private boolean isName(String keyword) {
		String nameRegex = "^[A-Za-z\\s]+$";
		Pattern pattern = Pattern.compile(nameRegex);
		return pattern.matcher(keyword).matches();
	}

	@Override
	public PurchasingHabits displayCustomerPurchasingHabit(Long customerId) throws ResourceNotFoundException {
		CustomerProfile existingCustomer = customerProfileRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
		return existingCustomer.getPurchasingHabits();
	}

	@Override
	public void updatePurchasingHabit(Long customerId) throws ResourceNotFoundException {
		CustomerProfile existingCustomer = customerProfileRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
		int numberOfPurchases = existingCustomer.getPurchaseHistory().size();
		if (numberOfPurchases <= 3) {
			existingCustomer.setPurchasingHabits(PurchasingHabits.NEW);
		} else if (numberOfPurchases > 3 && numberOfPurchases < 10) {
			existingCustomer.setPurchasingHabits(PurchasingHabits.SPARSE);
		} else if (numberOfPurchases >=10 && numberOfPurchases < 25) {
			existingCustomer.setPurchasingHabits(PurchasingHabits.REGULAR);
		} else {
			existingCustomer.setPurchasingHabits(PurchasingHabits.FREQUENT);
		}
	}

}
