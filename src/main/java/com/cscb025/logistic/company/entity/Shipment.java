package com.cscb025.logistic.company.entity;

import com.cscb025.logistic.company.enums.ShipmentStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String uid;

    @Column(nullable = false)
    @NonNull
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NonNull
    private ShipmentStatus status;

    @Column(nullable = false)
    private LocalDateTime dateCreated;

    private LocalDateTime dateReceived;

    private LocalDateTime dateUpdated;

    @PrePersist
    public void setCreatedDate() {
        this.dateCreated = LocalDateTime.now();
        this.dateUpdated = LocalDateTime.now();
    }

    @PreUpdate
    public void setUpdateDate() {
        this.dateUpdated = LocalDateTime.now();
    }

    //Relations
    @ManyToOne
    @JoinColumn(name = "office_id", nullable = false)
    @NonNull
    private Office office; //Where shipment was registered

    @ManyToOne
    @JoinColumn(name = "office_worker_id", nullable = false)
    @NonNull
    private Employee office_worker; //Who registered shipment

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    @NonNull
    private Client receiver;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    @NonNull
    private Client sender;
}
