package com.crm.scheduler;

import com.crm.dto.ScheduleConfigDTO;

public interface DynamicSchedulerService {

    ScheduleConfigDTO updateCronExpression(ScheduleConfigDTO scheduleConfigDTO);

    void restartScheduledTask(String cronExpression);
}
