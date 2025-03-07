package com.crm.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "customer_profile")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id", nullable = false)
    private Long customerID;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private ContactInfo contactInfo;

    @ElementCollection
    @CollectionTable(name = "purchase_history",joinColumns = @JoinColumn(name = "customer_id") )
    private List<String> purchaseHistory;

    @ElementCollection
    @CollectionTable(name = "customer_segmentation",joinColumns = @JoinColumn(name = "customer_id") )
    private List<String> segmentationData;



}