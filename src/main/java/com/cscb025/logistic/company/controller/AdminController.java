package com.cscb025.logistic.company.controller;

import com.cscb025.logistic.company.controller.request.admin.CompanyRequestDTO;
import com.cscb025.logistic.company.controller.request.admin.EditOfficeRequestDTO;
import com.cscb025.logistic.company.controller.request.admin.OfficeRequestDTO;
import com.cscb025.logistic.company.controller.response.ClientResponseDTO;
import com.cscb025.logistic.company.controller.response.admin.CompanyResponseDTO;
import com.cscb025.logistic.company.controller.response.EmployeeResponseDTO;
import com.cscb025.logistic.company.controller.response.admin.OfficeResponseDTO;
import com.cscb025.logistic.company.service.ClientService;
import com.cscb025.logistic.company.service.CompanyService;
import com.cscb025.logistic.company.service.EmployeeService;
import com.cscb025.logistic.company.service.OfficeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "*")
public class AdminController {

    private final CompanyService companyService;
    private final OfficeService officeService;
    private final EmployeeService employeeService;
    private final ClientService clientService;

    public AdminController(CompanyService companyService, OfficeService officeService, EmployeeService employeeService, ClientService clientService) {
        this.companyService = companyService;
        this.officeService = officeService;
        this.employeeService = employeeService;
        this.clientService = clientService;
    }

    @GetMapping("/employee/all")
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/client/all")
    public ResponseEntity<List<ClientResponseDTO>> getAllClients() {
        return ResponseEntity.ok(clientService.getAll());
    }

    @PostMapping("/company")
    public ResponseEntity<CompanyResponseDTO> registerCompany(@RequestBody @NotBlank String name) {
        return ResponseEntity.ok(companyService.register(name.trim()));
    }

    @PutMapping("/company")
    public ResponseEntity<CompanyResponseDTO> updateCompany(@RequestBody @Valid CompanyRequestDTO companyRequest) {
        return ResponseEntity.ok(companyService.edit(companyRequest));
    }

    @GetMapping("/company")
    public ResponseEntity<CompanyResponseDTO> getCompany(@RequestBody @NotBlank String companyId) {
        return ResponseEntity.ok(companyService.view(companyId));
    }

    @DeleteMapping("/company")
    public ResponseEntity<String> deleteCompany(@RequestBody @NotBlank String companyId) {
        return ResponseEntity.ok(companyService.delete(companyId));
    }


    @GetMapping("/office")
    public ResponseEntity<OfficeResponseDTO> getOffice(@RequestBody @NotBlank String officeId) {
        return ResponseEntity.ok(officeService.view(officeId));
    }

    @PostMapping("/office")
    public ResponseEntity<OfficeResponseDTO> registerOffice(@RequestBody @Valid OfficeRequestDTO officeRequest) {
        return ResponseEntity.ok(officeService.register(officeRequest));
    }

    @PutMapping("/office")
    public ResponseEntity<OfficeResponseDTO> updateOffice(@RequestBody @Valid EditOfficeRequestDTO officeRequest) {
        return ResponseEntity.ok(officeService.edit(officeRequest));
    }

    @DeleteMapping("/office")
    public ResponseEntity<String> deleteOffice(@RequestBody @NotBlank String officeId) {
        return ResponseEntity.ok(officeService.deleteOffice(officeId));
    }

}
