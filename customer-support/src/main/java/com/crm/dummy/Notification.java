package com.crm.dummy;
import com.crm.dto.NotificationDTO;
import org.springframework.stereotype.Component;

@Component
public class Notification {
    public NotificationDTO sendNotificatonDummy(NotificationDTO notificationDTO){
        notificationDTO.setStatus("SENT");
        return notificationDTO;
    }
}