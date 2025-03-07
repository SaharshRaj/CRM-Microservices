package com.crm.dto;

import com.crm.enums.Status;
import com.crm.enums.Type;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long notificationID;
    private Long customerID;
    private String message;
    private Type type;
    private Status status;
    private LocalDateTime sentAt;
}
