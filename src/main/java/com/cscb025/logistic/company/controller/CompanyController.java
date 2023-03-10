package com.cscb025.logistic.company.controller;

import lombok.AllArgsConstructor;

import com.cscb025.logistic.company.controller.request.admin.CompanyRequestDTO;
import com.cscb025.logistic.company.controller.response.admin.CompanyResponseDTO;
import com.cscb025.logistic.company.service.CompanyService;

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
@RequestMapping("/company")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyResponseDTO> register(@RequestBody @NotBlank String name) {
        return ResponseEntity.ok(companyService.register(name.trim()));
    }

    @PutMapping
    public ResponseEntity<CompanyResponseDTO> update(@RequestBody @Valid CompanyRequestDTO companyRequest) {
        return ResponseEntity.ok(companyService.edit(companyRequest));
    }

    @GetMapping("/{companyId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<CompanyResponseDTO> get(@PathVariable @NotBlank String companyId) {
        return ResponseEntity.ok(companyService.view(companyId));
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<String> delete(@PathVariable @NotBlank String companyId) {
        return ResponseEntity.ok(companyService.delete(companyId));
    }

}
