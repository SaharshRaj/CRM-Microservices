package com.crm.scheduler;

import com.crm.dto.NotificationDTO;

import java.util.List;

public interface SchedulerService {
    public List<NotificationDTO> sendNotifications();
}
