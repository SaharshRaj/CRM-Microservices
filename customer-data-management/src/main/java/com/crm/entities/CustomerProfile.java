package com.crm.entities;

import com.crm.enums.Interest;
import com.crm.enums.PurchasingHabits;
import com.crm.enums.Region;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a customer profile.
 */
@Entity
@Getter
@Setter
@Table(name = "customer_profile")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerProfile {

    /**
     * The unique ID of the customer.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id", nullable = false)
    private Long customerID;

    /**
     * The name of the customer.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * The emailID of the customer.
     */
    @Column(name = "emailId", nullable = false)
    private String emailId;

    /**
     * The phone number of the customer.
     */
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    /**
     * The purchase history of the customer.
     */
    @ElementCollection
    @CollectionTable(name = "purchase_history",joinColumns = @JoinColumn(name = "customer_id") )
    private List<String> purchaseHistory;

    /**
     * The segmentation data of the customer.
     */
    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "customer_segmentation",joinColumns = @JoinColumn(name = "customer_id") )
    private List<String> segmentationData = new ArrayList<>();

    /**
     * Sets the region of the customer.
     *
     * @param region the region to set
     */
    public void setRegion(Region region) {
        ensureSegmentationDataSize();
        segmentationData.set(0, region.name());
    }

    /**
     * Gets the region of the customer.
     *
     * @return the region of the customer
     */
    public Region getRegion() {
        return Region.valueOf(segmentationData.get(0));
    }

    /**
     * Sets the interest of the customer.
     *
     * @param interest the interest to set
     */
    public void setInterest(Interest interest) {
        ensureSegmentationDataSize();
        segmentationData.set(1, interest.name());
    }

    /**
     * Gets the interest of the customer.
     *
     * @return the interest of the customer
     */
    public Interest getInterest() {
        return Interest.valueOf(segmentationData.get(1));
    }

    /**
     * Sets the purchasing habits of the customer.
     *
     * @param purchasingHabits the purchasing habits to set
     */
    public void setPurchasingHabits(PurchasingHabits purchasingHabits) {
        ensureSegmentationDataSize();
        segmentationData.set(2, purchasingHabits.name());
    }

    /**
     * Gets the purchasing habits of the customer.
     *
     * @return the purchasing habits of the customer
     */
    public PurchasingHabits getPurchasingHabits() {
        return PurchasingHabits.valueOf(segmentationData.get(2));
    }

    /**
     * Ensures the segmentation data list has the required size.
     */
    private void ensureSegmentationDataSize() {
        while (segmentationData.size() < 3) {
            segmentationData.add("");
        }
    }

}