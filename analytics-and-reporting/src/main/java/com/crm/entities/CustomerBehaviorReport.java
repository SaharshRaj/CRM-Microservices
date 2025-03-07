package com.crm.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "customer_behavior_report")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerBehaviorReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id",nullable = false)
    private Long reportID;
    //Foreign key
    @Column(name = "customer_id")
    private Long customerID;

    @ElementCollection
    @CollectionTable(name = "data-points", joinColumns = @JoinColumn(name = "report_id"))
    @Column(name = "data_point")
    private List<String> dataPoints;
}