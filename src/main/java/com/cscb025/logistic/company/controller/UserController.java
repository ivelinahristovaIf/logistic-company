package com.cscb025.logistic.company.controller;

import com.cscb025.logistic.company.controller.request.user.ClientEditRequestDTO;
import com.cscb025.logistic.company.controller.request.user.ClientRegistrationRequestDTO;
import com.cscb025.logistic.company.controller.request.user.EmployeeEditRequestDTO;
import com.cscb025.logistic.company.controller.request.user.EmployeeRegistrationRequestDTO;
import com.cscb025.logistic.company.controller.request.user.UserLoginRequestDTO;
import com.cscb025.logistic.company.controller.response.ClientResponseDTO;
import com.cscb025.logistic.company.controller.response.EmployeeResponseDTO;
import com.cscb025.logistic.company.controller.response.user.UserLoginResponseDTO;
import com.cscb025.logistic.company.controller.response.user.UserRegistrationResponseDTO;
import com.cscb025.logistic.company.exception.TokenExpiredException;
import com.cscb025.logistic.company.service.ClientService;
import com.cscb025.logistic.company.service.EmployeeService;
import com.cscb025.logistic.company.service.UserService;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin(origins = "*")
public class UserController {

    private final EmployeeService employeeService;

    private final ClientService clientService;

    private final UserService userService;

    public UserController(EmployeeService employeeService, ClientService clientService, UserService userService) {
        this.employeeService = employeeService;
        this.clientService = clientService;
        this.userService = userService;
    }

    @PostMapping("/employee")
    public ResponseEntity<UserRegistrationResponseDTO> registerEmployee(
            @ModelAttribute("userForm") @RequestBody EmployeeRegistrationRequestDTO userForm) {
        UserRegistrationResponseDTO responseDTO = employeeService.register(userForm);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/employee")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<UserRegistrationResponseDTO> updateEmployee(
            @RequestBody @Valid EmployeeEditRequestDTO employeeEditRequest) {
        return ResponseEntity.ok(employeeService.edit(employeeEditRequest));
    }

    @GetMapping("/employee/{uid}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<UserLoginResponseDTO> viewEmployee(@PathVariable @NotBlank String uid) {
        return ResponseEntity.ok(employeeService.view(uid.trim()));
    }

    @DeleteMapping("/employee/{uid}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<String> deleteEmployee(@PathVariable @NotBlank String uid) {
        return ResponseEntity.ok(employeeService.delete(uid.trim()));
    }

    @GetMapping("/employee")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @PostMapping("/client")
    public ResponseEntity<UserRegistrationResponseDTO> registerClient(
            @RequestBody @Valid ClientRegistrationRequestDTO user) {
        return ResponseEntity.ok(clientService.register(user));
    }

    @PutMapping("/client")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<UserRegistrationResponseDTO> updateClient(
            @RequestBody @Valid ClientEditRequestDTO clientEditRequest) {
        return ResponseEntity.ok(clientService.edit(clientEditRequest));
    }

    @GetMapping("/client/{uid}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<UserLoginResponseDTO> viewClient(@PathVariable @NotBlank String uid) {
        return ResponseEntity.ok(clientService.view(uid.trim()));
    }

    @DeleteMapping("/client/{uid}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<String> deleteClient(@PathVariable @NotBlank String uid) {
        return ResponseEntity.ok(clientService.delete(uid.trim()));
    }

    @GetMapping("/client")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<List<ClientResponseDTO>> getAllClients() {
        return ResponseEntity.ok(clientService.getAll());
    }

    @PostMapping("/signin")
    public ResponseEntity<UserRegistrationResponseDTO> login(
            @RequestBody UserLoginRequestDTO loginRequest) throws TokenExpiredException {
        UserRegistrationResponseDTO responseDTO = userService.getUserLoginResponse(loginRequest);
        return ResponseEntity.ok(responseDTO);
    }

}
