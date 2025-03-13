package com.crm.dummy;

import com.crm.dto.NotificationDTO;
import org.springframework.stereotype.Component;

@Component
public class DummyClass {
    public NotificationDTO sendNotificatonDummy(NotificationDTO notificationDTO){
        notificationDTO.setStatus("SENT");
        return notificationDTO;
    }
}
