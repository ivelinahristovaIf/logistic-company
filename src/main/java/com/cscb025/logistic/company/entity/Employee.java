package com.cscb025.logistic.company.entity;

import com.cscb025.logistic.company.enums.EmployeeRole;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "employees")
public class Employee extends User {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String uid;

    @Column
    private String name;

    @NonNull
    @Column(nullable = false, unique = true)
    private String email;

    @NonNull
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @NonNull
    private EmployeeRole role;

    //Relations
    @ManyToOne
    @JoinColumn(name = "office_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Office office;

    @OneToMany(mappedBy = "office_worker")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Shipment> registeredShipments = new ArrayList<>(); //registered shipments by office worker
}
