package com.cscb025.logistic.company.controller;

import com.cscb025.logistic.company.controller.request.shipment.RegisterShipmentRequestDTO;
import com.cscb025.logistic.company.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/shipment")
@CrossOrigin(origins = "*")
public class ShipmentController {

    private final ShipmentService shipmentService;

    @Autowired
    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public String registerView() {
        return "employee/registerShipment";
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity register(@RequestBody @Valid RegisterShipmentRequestDTO registerShipmentRequest,
                                   @RequestAttribute("userId") String userId) {
        return ResponseEntity.ok(shipmentService.register(registerShipmentRequest, userId));
    }
}
