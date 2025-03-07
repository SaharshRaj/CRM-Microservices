package com.crm.controller;

import com.crm.dto.NotificationDTO;
import com.crm.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NotificationControllerImpl implements NotificationController {
    
    @Autowired
    private NotificationService service;
    
    /**
     * Add JavaDoc
     */
    @Override
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        List<NotificationDTO> notificationDTOS = service.retrieveAllNotifications();
        return new ResponseEntity<>(notificationDTOS, HttpStatus.OK);
    }
}
