package com.crm.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "sales_performance_report")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesPerformanceReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id",nullable = false)
    private Long reportID;
    //Foreign Key
    @Column(name = "customer_id")
    private Long customerID;
    //Foreign Key
    @Column(name = "opportunity_id")
    private Long opportunityID;

    @ElementCollection
    @CollectionTable(name = "data-points", joinColumns = @JoinColumn(name = "report_id"))
    @Column(name = "data_point")
    private List<String> dataPoints;

}