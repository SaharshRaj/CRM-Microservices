package com.crm.feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import com.crm.dto.NotificationDTO;
@FeignClient("notification-service")
public interface NotificationClient {

	
	@PostMapping("/notificaion/send")
	void sendNotification(NotificationDTO notificationDTO);
}
