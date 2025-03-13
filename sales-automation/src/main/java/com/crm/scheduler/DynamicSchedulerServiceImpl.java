package com.crm.scheduler;

import com.crm.dto.ScheduleConfigDTO;
import com.crm.entities.ScheduleConfig;
import com.crm.exception.InvalidCronExpressionException;
import com.crm.mapper.SalesOpportunityMapper;
import com.crm.repository.ScheduleConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
public class DynamicSchedulerServiceImpl implements DynamicSchedulerService {


    private final ScheduleConfigRepository scheduleConfigRepository;
    private final SchedulerService schedulerService;
    private final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    private ScheduledFuture<?> scheduledTask;


    @Autowired
    public DynamicSchedulerServiceImpl(ScheduleConfigRepository repository, SchedulerServiceImpl schedulerService){
        this.scheduleConfigRepository = repository;
        this.schedulerService = schedulerService;
        taskScheduler.initialize();
    }

    @Override
    public ScheduleConfigDTO updateCronExpression(ScheduleConfigDTO scheduleConfigDTO) {
        String newCron = SalesOpportunityMapper.MAPPER.mapToScheduleConfig(scheduleConfigDTO).getCronExpression();
        ScheduleConfig config = scheduleConfigRepository.findByTaskName("Send Reminder")
                .orElse(new ScheduleConfig());
        if(CronExpression.isValidExpression(newCron)){
            ZonedDateTime nextExecution = CronExpression.parse(newCron).next(ZonedDateTime.now());
            config.setCronExpression(newCron);
            ScheduleConfig saved = scheduleConfigRepository.save(config);
            restartScheduledTask(newCron);
            log.info("Next execution scheduled for {}", nextExecution);
            return SalesOpportunityMapper.MAPPER.mapToScheduleConfigDTO(saved);
    }
        else{
            log.info("Invalid Cron Expression {}", newCron);
            throw new InvalidCronExpressionException("Invalid Cron Expression "+newCron);
        }
        }

    @Override
    public void restartScheduledTask(String cronExpression) {
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
        }
        scheduledTask = taskScheduler.schedule(schedulerService::sendNotifications, new CronTrigger(cronExpression));
    }


}