package com.crm.feign;

//import com.crm.dto.CustomerProfileDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.crm.dto.EmailDTO; 

@FeignClient("api-gateway")
public interface CustomerDataManagement {
//	@PostMapping("/notification/send")
//	void sendNotifications(@RequestBody NotificationDTO notificationDTO);
}
