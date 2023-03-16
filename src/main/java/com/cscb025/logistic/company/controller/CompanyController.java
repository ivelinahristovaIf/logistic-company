package com.cscb025.logistic.company.controller;

import com.cscb025.logistic.company.controller.response.admin.CompanyResponseDTO;
import com.cscb025.logistic.company.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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

    @PutMapping("/{companyId}")
    public ResponseEntity<CompanyResponseDTO> update(@PathVariable("companyId") String uid, @RequestBody @Valid String name) {
        return ResponseEntity.ok(companyService.edit(uid, name));
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
