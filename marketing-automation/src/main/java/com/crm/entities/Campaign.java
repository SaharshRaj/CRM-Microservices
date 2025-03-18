package com.crm.entities;

import com.crm.enums.Type; 

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "campaign")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_id",nullable = false)
    private Long campaignID;
    @Column(unique=true) 
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    private Type type;
    private int customerInteractions=0;
    @Column(name="tracking_url")
    private String trackingUrl;

}