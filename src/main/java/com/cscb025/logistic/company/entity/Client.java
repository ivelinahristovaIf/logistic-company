package com.cscb025.logistic.company.entity;

import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "clients")
public class Client extends User{

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String uid;

    @Column(nullable = false)
    @NonNull
    private String name;

    @NonNull
    @Column(nullable = false, unique = true)
    private String email;

    @NonNull
    @Column(nullable = false)
    private String password;

    @NonNull
    @Column(nullable = false, unique = true)
    private String phone;

    //Relations
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    @NonNull
    private Company company;

    @OneToMany(mappedBy = "receiver")
    private List<Shipment> receivedShipments = new ArrayList<>();

    @OneToMany(mappedBy = "sender")
    private List<Shipment> sendShipments = new ArrayList<>();

}
