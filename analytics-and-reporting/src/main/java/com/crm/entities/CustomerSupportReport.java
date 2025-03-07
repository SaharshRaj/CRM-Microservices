package com.crm.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "customer_support_report")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerSupportReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id",nullable = false)
    private Long reportID;
    //Foreign Key
    @Column(name = "ticket_id")
    private Long ticketID;

    @ElementCollection
    @CollectionTable(name = "data-points", joinColumns = @JoinColumn(name = "report_id"))
    @Column(name = "data_point")
    private List<String> dataPoints;
}