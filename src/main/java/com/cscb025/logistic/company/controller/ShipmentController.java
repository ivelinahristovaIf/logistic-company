package com.cscb025.logistic.company.controller;

import lombok.AllArgsConstructor;

import com.cscb025.logistic.company.config.JwtRequestFilter;
import com.cscb025.logistic.company.config.JwtTokenUtil;
import com.cscb025.logistic.company.controller.request.shipment.EditShipmentDTO;
import com.cscb025.logistic.company.controller.request.shipment.RegisterShipmentRequestDTO;
import com.cscb025.logistic.company.controller.response.shipment.ShipmentDTO;
import com.cscb025.logistic.company.controller.response.shipment.ShipmentRegisterResponseDTO;
import com.cscb025.logistic.company.service.ShipmentService;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shipment")
@CrossOrigin(origins = "*")
@AllArgsConstructor
@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
public class ShipmentController {

    private final ShipmentService shipmentService;

    private final JwtTokenUtil jwtTokenUtil;

    private final JwtRequestFilter jwtRequestFilter;

    @GetMapping
    public String registerView() {
        return "employee/registerShipment";
    }

    @PostMapping
    public ResponseEntity<ShipmentRegisterResponseDTO> register(
            @RequestBody @Valid RegisterShipmentRequestDTO registerShipmentRequest) {
        final String userId = jwtTokenUtil.extractUUID(this.jwtRequestFilter.getMemberToken());
        return ResponseEntity.ok(shipmentService.register(registerShipmentRequest, userId));
    }

    @PutMapping
    public ResponseEntity<ShipmentDTO> update(@RequestBody @Valid EditShipmentDTO registerShipmentRequest) {
        return ResponseEntity.ok(shipmentService.update(registerShipmentRequest));
    }

    @GetMapping("/{uid}")
    public ResponseEntity<ShipmentDTO> view(@PathVariable @NotBlank final String uid) {
        return ResponseEntity.ok(shipmentService.findById(uid));
    }

    @DeleteMapping("/{uid}")
    public ResponseEntity<ShipmentDTO> delete(@PathVariable @NotBlank final String uid) {
        return ResponseEntity.ok(shipmentService.delete(uid));
    }

    @GetMapping
    public ResponseEntity<List<ShipmentDTO>> viewAll() {
        return ResponseEntity.ok(shipmentService.findAll());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<ShipmentDTO>> viewByEmployee(@PathVariable @NotBlank final String employeeId) {
        return ResponseEntity.ok(shipmentService.findAllByEmployee(employeeId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ShipmentDTO>> viewAllByStatus(@PathVariable @NotBlank final String status) {
        return ResponseEntity.ok(shipmentService.findAllByStatus(status));
    }

    @GetMapping("/sender/{senderId}")
    public ResponseEntity<List<ShipmentDTO>> viewAllBySender(@PathVariable @NotBlank final String senderId) {
        return ResponseEntity.ok(shipmentService.findAllBySender(senderId));
    }

    @GetMapping("/receiver/{userId}")
    public ResponseEntity<List<ShipmentDTO>> viewAllByReceiver(@PathVariable @NotBlank final String userId) {
        return ResponseEntity.ok(shipmentService.findAllByReceiver(userId));
    }
}
