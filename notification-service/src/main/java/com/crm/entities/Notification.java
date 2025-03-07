package com.crm.entities;

import com.crm.enums.Status;
import com.crm.enums.Type;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "notification")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id",nullable = false)
    private Long notificationID;

    //Foreign key
    @Column(name = "customer_id")
    private Long customerID;

    @Column(name = "message")
    private String message;

    @Enumerated(EnumType.STRING)
    private Type type;
    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime sentAt;

}