package com.crm.feign;

import com.crm.dto.CustomerProfileDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

// All request go through api-gateway
@FeignClient("api-gateway")
public interface CustomerDataManagement {
    //All the methods you want to use
    @GetMapping("api/customers")
    public ResponseEntity<List<CustomerProfileDTO>> getAllCustomerProfiles();
}
