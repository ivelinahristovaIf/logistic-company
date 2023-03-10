package com.cscb025.logistic.company.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "clients")
public class Client extends User {

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
    @ManyToOne(cascade = javax.persistence.CascadeType.ALL, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    @NonNull
    private Company company;

    @OneToMany(mappedBy = "receiver")
    private Set<Shipment> receivedShipments = new HashSet<>();

    @OneToMany(mappedBy = "sender")
    private Set<Shipment> sendShipments = new HashSet<>();

}
