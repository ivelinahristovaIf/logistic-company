package com.cscb025.logistic.company.service;

import com.cscb025.logistic.company.controller.request.shipment.RegisterShipmentRequestDTO;
import com.cscb025.logistic.company.controller.response.ClientResponseDTO;
import com.cscb025.logistic.company.controller.response.EmployeeResponseDTO;
import com.cscb025.logistic.company.controller.response.shipment.ShipmentRegisterResponseDTO;
import com.cscb025.logistic.company.entity.Client;
import com.cscb025.logistic.company.entity.Employee;
import com.cscb025.logistic.company.entity.Office;
import com.cscb025.logistic.company.entity.Shipment;
import com.cscb025.logistic.company.enums.ShipmentStatus;
import com.cscb025.logistic.company.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final EmployeeService employeeService;
    private final ClientService clientService;

    @Autowired
    public ShipmentService(ShipmentRepository shipmentRepository, EmployeeService employeeService, ClientService clientService) {
        this.shipmentRepository = shipmentRepository;
        this.employeeService = employeeService;
        this.clientService = clientService;
    }

    public Object register(RegisterShipmentRequestDTO registerShipmentRequest, String loggedUserID) {
        Employee officeWorker = employeeService.getEmployee(loggedUserID);
        Client receiver = clientService.getClient(registerShipmentRequest.getReceiver());
        Client sender = clientService.getClient(registerShipmentRequest.getSender());
        Shipment shipment = new Shipment(registerShipmentRequest.getTotalPrice(), ShipmentStatus.REGISTERED, officeWorker.getOffice(),
                officeWorker, receiver, sender);
        shipment = shipmentRepository.save(shipment);
        return new ShipmentRegisterResponseDTO(shipment.getUid(),
                shipment.getTotalPrice(),
                officeWorker.getOffice().getName(),
                new EmployeeResponseDTO(officeWorker.getEmail(), officeWorker.getName(), officeWorker.getRole(), officeWorker.getOffice().getName()),
                new ClientResponseDTO(receiver.getEmail(), receiver.getName(), receiver.getPhone()),
                new ClientResponseDTO(sender.getEmail(), sender.getName(), sender.getPhone()));
    }
}
