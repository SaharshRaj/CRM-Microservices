package com.crm.service;

import com.crm.dto.NotificationDTO;

import java.util.List;


public interface NotificationService {
    public List<NotificationDTO> retrieveAllNotifications();
}
