package com.cscb025.logistic.company.controller.response.shipment;

import com.cscb025.logistic.company.controller.response.ClientResponseDTO;
import com.cscb025.logistic.company.controller.response.EmployeeResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentRegisterResponseDTO {

    private String uid;

    private Double totalPrice;

    private String officeName;

    private EmployeeResponseDTO officeWorker;

    private ClientResponseDTO receiver;

    private ClientResponseDTO sender;
}
