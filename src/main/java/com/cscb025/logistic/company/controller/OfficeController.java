package com.cscb025.logistic.company.controller;

import lombok.AllArgsConstructor;

import com.cscb025.logistic.company.controller.request.admin.EditOfficeRequestDTO;
import com.cscb025.logistic.company.controller.request.admin.OfficeRequestDTO;
import com.cscb025.logistic.company.controller.response.admin.OfficeResponseDTO;
import com.cscb025.logistic.company.service.OfficeService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/office")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class OfficeController {

    private final OfficeService officeService;

    @GetMapping("/{officeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<OfficeResponseDTO> get(@PathVariable @NotBlank String officeId) {
        return ResponseEntity.ok(officeService.view(officeId));
    }

    @PostMapping
    public ResponseEntity<OfficeResponseDTO> register(@RequestBody @Valid OfficeRequestDTO officeRequest) {
        return ResponseEntity.ok(officeService.register(officeRequest));
    }

    @PutMapping
    public ResponseEntity<OfficeResponseDTO> update(@RequestBody @Valid EditOfficeRequestDTO officeRequest) {
        return ResponseEntity.ok(officeService.edit(officeRequest));
    }

    @DeleteMapping("/{officeId}")
    public ResponseEntity<String> delete(@PathVariable @NotBlank String officeId) {
        return ResponseEntity.ok(officeService.deleteOffice(officeId));
    }

}
