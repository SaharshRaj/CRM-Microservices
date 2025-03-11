package com.crm.service;

import com.crm.dto.CustomerProfileDTO;
import com.crm.enums.PurchasingHabits;
import com.crm.exception.ResourceNotFoundException;

import java.util.List;


public interface CustomerService {
	public List<CustomerProfileDTO> retrieveAllProfiles() throws ResourceNotFoundException;
	public CustomerProfileDTO getCustomerById(Long customerId) throws ResourceNotFoundException;
	public CustomerProfileDTO updateCustomer(Long customerId, CustomerProfileDTO customerProfileDTO) throws ResourceNotFoundException;
	public void deleteCustomer(Long customerId) throws ResourceNotFoundException;
	public List<CustomerProfileDTO> searchCustomers(String keyword);
	//public String getCustomerInteractionHistory(Long customerId) throws ResourceNotFoundException;
	public PurchasingHabits displayCustomerPurchasingHabit(Long customerId) throws ResourceNotFoundException ; 
	public void updatePurchasingHabit(Long customerId) throws ResourceNotFoundException ;
	public List<CustomerProfileDTO> searchCustomerBasedOnEmailId(String email) throws ResourceNotFoundException;
	public List<CustomerProfileDTO> searchCustomerBasedOnName(String name) throws ResourceNotFoundException;
	public List<CustomerProfileDTO> searchCustomerBasedOnPhoneNumber(String phoneNumber) throws ResourceNotFoundException;
}
