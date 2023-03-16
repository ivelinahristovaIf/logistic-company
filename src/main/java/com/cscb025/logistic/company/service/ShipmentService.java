package com.cscb025.logistic.company.service;

import com.cscb025.logistic.company.controller.request.shipment.EditShipmentDTO;
import com.cscb025.logistic.company.controller.request.shipment.RegisterShipmentRequestDTO;
import com.cscb025.logistic.company.controller.response.ClientResponseDTO;
import com.cscb025.logistic.company.controller.response.EmployeeResponseDTO;
import com.cscb025.logistic.company.controller.response.shipment.ShipmentDTO;
import com.cscb025.logistic.company.controller.response.shipment.ShipmentRegisterResponseDTO;
import com.cscb025.logistic.company.entity.Client;
import com.cscb025.logistic.company.entity.Employee;
import com.cscb025.logistic.company.entity.Office;
import com.cscb025.logistic.company.entity.Shipment;
import com.cscb025.logistic.company.enums.ShipmentStatus;
import com.cscb025.logistic.company.exception.EntityNotFoundException;
import com.cscb025.logistic.company.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Double revenue(LocalDate startDate, LocalDate endDate) {
        List<Shipment> receivedIsBetween = shipmentRepository.findAllByDateReceivedIsBetween(startDate, endDate);
        return receivedIsBetween.stream().map(Shipment::getTotalPrice).reduce(0.0, Double::sum);
    }

    public ShipmentDTO findById(final String shipmentUid) {
        Shipment shipment = getShipment(shipmentUid);
        return mapToDto(shipment);
    }

    public ShipmentRegisterResponseDTO register(RegisterShipmentRequestDTO registerShipmentRequest,
                                                String loggedUserID) {
        final Employee officeWorker = employeeService.getEmployee(loggedUserID);
        final Client receiver = clientService.getClient(registerShipmentRequest.getReceiver());
        final Client sender = clientService.getClient(registerShipmentRequest.getSender());
        final Shipment shipment = new Shipment(registerShipmentRequest.getTotalPrice(), ShipmentStatus.REGISTERED,
                officeWorker.getOffice(), officeWorker, receiver, sender);
        Shipment shipmentResponse = shipmentRepository.save(shipment);
        return new ShipmentRegisterResponseDTO(shipmentResponse.getUid(), shipmentResponse.getTotalPrice(),
                officeWorker.getOffice().getName(),
                new EmployeeResponseDTO(officeWorker.getEmail(), officeWorker.getName(), officeWorker.getRole(),
                        officeWorker.getOffice().getName()),
                new ClientResponseDTO(receiver.getEmail(), receiver.getName(), receiver.getPhone()),
                new ClientResponseDTO(sender.getEmail(), sender.getName(), sender.getPhone()));
    }

    public ShipmentDTO update(final String shipmentId, final String status) {
        Shipment shipment = getShipment(shipmentId);
        shipment.setStatus(ShipmentStatus.valueOf(status));
        shipment = shipmentRepository.save(shipment);
        return mapToDto(shipment);
    }

    public ShipmentDTO delete(final String uid) {
        final Shipment shipment = getShipment(uid);
        shipmentRepository.delete(shipment);
        return mapToDto(shipment);
    }

    private ShipmentDTO mapToDto(final Shipment shipment) {
        final Client receiver = shipment.getReceiver();
        final Client sender = shipment.getSender();
        final Office office = shipment.getOffice();
        final Employee officeWorker = shipment.getOfficeWorker();
        return ShipmentDTO
                .builder()
                .totalPrice(shipment.getTotalPrice())
                .officeName(office.getName())
                .officeWorker(
                        new EmployeeResponseDTO(officeWorker.getEmail(), officeWorker.getName(), officeWorker.getRole(),
                                officeWorker.getOffice().getName()))
                .receiver(new ClientResponseDTO(receiver.getEmail(), receiver.getName(), receiver.getPhone()))
                .sender(new ClientResponseDTO(sender.getEmail(), sender.getName(), sender.getPhone()))
                .build();
    }

    private Shipment getShipment(String uid) {
        final Optional<Shipment> employeeOptional = shipmentRepository.findById(uid);
        if (!employeeOptional.isPresent()) {
            throw new EntityNotFoundException("No such shipment in the system!");
        }
        return employeeOptional.get();
    }

    public List<ShipmentDTO> findAll() {
        return shipmentRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<ShipmentDTO> findAllByEmployee(final String uid) {
        final Employee officeWorker = employeeService.getEmployee(uid);
        return shipmentRepository.findAllByOfficeWorker(officeWorker).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<ShipmentDTO> findAllByStatus(final String status) {
        return shipmentRepository.findAllByStatus(ShipmentStatus.NOT_RECEIVED).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<ShipmentDTO> findAllBySender(final String userId) {
        final Client client = clientService.getClient(userId);
        return shipmentRepository.findAllBySender(client).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<ShipmentDTO> findAllByReceiver(final String userId) {
        return shipmentRepository
                .findAllByReceiver(clientService.getClient(userId))
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
