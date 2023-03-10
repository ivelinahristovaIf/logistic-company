package com.cscb025.logistic.company.controller.request.shipment;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RegisterShipmentRequestDTO {

    @NotNull(message = "Total price must not be empty!")
    private Double totalPrice;

    @NotBlank(message = "Office must not be empty!")
    private String officeId;

    @NotBlank(message = "Receiver must not be empty!")
    private String receiver;

    @NotBlank(message = "Sender must not be empty!")
    private String sender;

    private boolean isSent;

    private boolean isReceived;

}
