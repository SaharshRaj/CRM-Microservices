package com.crm.service;

import com.crm.dto.NotificationDTO;
import com.crm.entities.Notification;
import com.crm.mapper.NotificationMapper;
import com.crm.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    
    @Autowired
    private NotificationRepository repository;
    
    /**
     * Add Method Info for documentation
     */
    @Override
    public List<NotificationDTO> retrieveAllNotifications() {
        // Retreive all campaigns from repo
        List<Notification> allNotifications = repository.findAll();

        // Declare DTO list
        List<NotificationDTO> resultList = new ArrayList<>();

        // Populate List
        allNotifications.forEach(e -> {
            // Use MAPPER method to convert entity to DTO
            NotificationDTO notificationDTO = NotificationMapper.MAPPER.mapToDTO(e);
            // Add DTO to list
            resultList.add(notificationDTO);
        });
        //Return Result
        return resultList;
    }
}
