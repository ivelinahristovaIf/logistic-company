package com.cscb025.logistic.company.controller.response.shipment;

import lombok.Builder;
import lombok.Data;

import com.cscb025.logistic.company.controller.response.ClientResponseDTO;
import com.cscb025.logistic.company.controller.response.EmployeeResponseDTO;
import com.cscb025.logistic.company.enums.ShipmentStatus;

@Data
@Builder
public class ShipmentDTO {

    private Double totalPrice;

    private String officeName;

    private ShipmentStatus status;

    private EmployeeResponseDTO officeWorker;

    private ClientResponseDTO receiver;

    private ClientResponseDTO sender;
}
