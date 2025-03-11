package com.crm.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.crm.enums.Interest;
import com.crm.enums.PurchasingHabits;
import com.crm.enums.Region;

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

    @Column(name = "emailId", nullable = false)
    private String emailId;
    
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @ElementCollection
    @CollectionTable(name = "purchase_history",joinColumns = @JoinColumn(name = "customer_id") )
    private List<String> purchaseHistory;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "customer_segmentation",joinColumns = @JoinColumn(name = "customer_id") )
    private List<String> segmentationData = new ArrayList<>();
    
    public void setRegion(Region region) {
        ensureSegmentationDataSize();
        segmentationData.set(0, region.name());
    }

    public Region getRegion() {
        return Region.valueOf(segmentationData.get(0));
    }

    public void setInterest(Interest interest) {
        ensureSegmentationDataSize();
        segmentationData.set(1, interest.name());
    }

    public Interest getInterest() {
        return Interest.valueOf(segmentationData.get(1));
    }

    public void setPurchasingHabits(PurchasingHabits purchasingHabits) {
        ensureSegmentationDataSize();
        segmentationData.set(2, purchasingHabits.name());
    }

    public PurchasingHabits getPurchasingHabits() {
        return PurchasingHabits.valueOf(segmentationData.get(2));
    }

    private void ensureSegmentationDataSize() {
        while (segmentationData.size() < 3) {
            segmentationData.add("");
        }
    }

}