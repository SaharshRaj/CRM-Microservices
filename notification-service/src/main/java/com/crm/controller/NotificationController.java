package com.crm.controller;

import com.crm.dto.NotificationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("api/notifications")
public interface NotificationController {

    @GetMapping("")
    public ResponseEntity<List<NotificationDTO>> getAllNotifications();

}
