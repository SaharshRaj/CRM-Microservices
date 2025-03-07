package com.crm.mapper;

import com.crm.dto.NotificationDTO;
import com.crm.entities.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NotificationMapper {

    //NotificationMapper using Factory
    NotificationMapper MAPPER = Mappers.getMapper(NotificationMapper.class);


    //Notification to NotificationDTO
    NotificationDTO mapToDTO(Notification notification);
    //NotificationDTO to Notification
    Notification mapToNotification(NotificationDTO notificationDTO);
}
