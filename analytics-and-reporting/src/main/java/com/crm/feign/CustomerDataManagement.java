package com.crm.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

// All the requests are processed through api-gateway
@FeignClient("api-gateway")
public interface CustomerDataManagement {
    //All the methods you want to use

    //Mapping defined in Customer Controller
//    @GetMapping("api/customers")
//    public ResponseEntity<List<CustomerProfileDTO>> getAllCustomerProfiles();
}
