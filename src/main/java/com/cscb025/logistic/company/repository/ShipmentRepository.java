package com.cscb025.logistic.company.repository;

import com.cscb025.logistic.company.entity.Client;
import com.cscb025.logistic.company.entity.Employee;
import com.cscb025.logistic.company.entity.Shipment;
import com.cscb025.logistic.company.enums.ShipmentStatus;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, String> {

    List<Shipment> findAllByOfficeWorker(final Employee officeWorker);

    List<Shipment> findAllByStatus(final ShipmentStatus status);

    List<Shipment> findAllBySender(final Client sender);

    List<Shipment> findAllByReceiver(final Client receiver);

    List<Shipment> findAllByDateReceivedIsBetween(final LocalDate startDate, final LocalDate endDate);

}
