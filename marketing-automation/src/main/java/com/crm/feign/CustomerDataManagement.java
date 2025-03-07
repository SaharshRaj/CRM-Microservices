package com.crm.feign;

//import com.crm.dto.CustomerProfileDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient("api-gateway")
public interface CustomerDataManagement {
    //All the methods you wnat to use
//    @GetMapping("")
//    public ResponseEntity<List<CustomerProfileDTO>> getAllCustomerProfiles();
}
