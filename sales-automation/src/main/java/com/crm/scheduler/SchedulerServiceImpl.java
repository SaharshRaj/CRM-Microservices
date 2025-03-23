package com.crm.scheduler;

import com.crm.dto.NotificationDTO;
import com.crm.dummy.DummyClass;
import com.crm.entities.EmailFormat;
import com.crm.entities.SalesOpportunity;
import com.crm.enums.SalesStage;
import com.crm.repository.SalesOpportunityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SchedulerServiceImpl implements SchedulerService {

    private final SalesOpportunityRepository repository;
    private final DummyClass dummyClass;

    @Autowired
    public SchedulerServiceImpl(SalesOpportunityRepository repository, DummyClass dummyClass) {
        this.repository = repository;
        this.dummyClass = dummyClass;
    }

    @Override
    public List<NotificationDTO> sendFollowUpReminder() {
        log.info("STARTING CRON JOB -> SEND NOTIFICATIONS");
        List<SalesOpportunity> salesOpportunityList = repository.findByFollowUpReminder(LocalDate.now());
        List<NotificationDTO> result = new ArrayList<>();
        salesOpportunityList.forEach(
                o ->
                {
                    EmailFormat email = EmailFormat.builder()
                            .salutation("Dear employee,")
                            .openingLine("I hope this message finds you well.")
                            .body("This is to inform you that Lead #" + o.getOpportunityID() + " is closing on " + o.getClosingDate() + ".")
                            .conclusion("THIS IS AN AUTO-GENERATED EMAIL. PLEASE DO NOT REPLY ON THIS!")
                            .closing("SALES-AUTOMATION SERVICE \n CRM")
                            .build();

                    NotificationDTO notificationDTO = NotificationDTO.builder()
                            .subject("Follow-up Reminder for Sales Lead with ID " + o.getOpportunityID())
                            .body(email.toString())
                            .build();
                    NotificationDTO resultDTO = dummyClass.sendNotificatonDummy(notificationDTO);
                    result.add(resultDTO);
                    if (resultDTO.getStatus().equals("SENT")) {
                        log.info("NOTIFICATION SENT FOR LEAD WITH ID #{}", o.getOpportunityID());
                    } else {
                        log.error("FAILED TO SEND NOTIFICATION FOR LEAD WITH ID #{}", o.getOpportunityID());
                    }
                }

        );
        return result;
    }

    @Override
    public List<NotificationDTO> sendClosingNotification() {
        List<SalesOpportunity> salesOpportunityList = repository.findByClosingDate(LocalDate.now());
        List<NotificationDTO> result = new ArrayList<>();
        salesOpportunityList.forEach(
                o ->
                {
                    EmailFormat email = EmailFormat.builder()
                            .salutation("Dear employee,")
                            .openingLine("I hope this message finds you well.")
                            .body("This is to inform you that Lead #" + o.getOpportunityID() + " is closed at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + ".")
                            .conclusion("THIS IS AN AUTO-GENERATED EMAIL. PLEASE DO NOT REPLY ON THIS!")
                            .closing("SALES-AUTOMATION SERVICE \n CRM")
                            .build();

                    o.setSalesStage(SalesStage.CLOSED_LOST);

                    NotificationDTO notificationDTO = NotificationDTO.builder()
                            .subject("Status updated for Sales Lead with ID " + o.getOpportunityID())
                            .body(email.toString())
                            .build();
                    NotificationDTO resultDTO = dummyClass.sendNotificatonDummy(notificationDTO);
                    result.add(resultDTO);
                    if (resultDTO.getStatus().equals("SENT")) {
                        log.info("NOTIFICATION SENT FOR LEAD WITH ID #{}", o.getOpportunityID());
                    } else {
                        log.error("FAILED TO SEND NOTIFICATION FOR LEAD WITH ID #{}", o.getOpportunityID());
                    }
                }

        );
        return result;

    }

}