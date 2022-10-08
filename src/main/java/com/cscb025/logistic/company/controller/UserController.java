package com.cscb025.logistic.company.controller;

import com.cscb025.logistic.company.controller.request.user.*;
import com.cscb025.logistic.company.controller.response.user.UserLoginResponseDTO;
import com.cscb025.logistic.company.controller.response.user.UserRegistrationResponseDTO;
import com.cscb025.logistic.company.exception.TokenExpiredException;
import com.cscb025.logistic.company.service.ClientService;
import com.cscb025.logistic.company.service.EmployeeService;
import com.cscb025.logistic.company.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Controller
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    private final EmployeeService employeeService;
    private final ClientService clientService;
    private final UserService userService;

    @Autowired
    public UserController(EmployeeService employeeService, ClientService clientService, UserService userService) {
        this.employeeService = employeeService;
        this.clientService = clientService;
        this.userService = userService;
    }


    @PostMapping("/employee")
    public ResponseEntity<UserRegistrationResponseDTO> registerEmployee(@ModelAttribute("userForm") EmployeeRegistrationRequestDTO userForm) {
        UserRegistrationResponseDTO responseDTO = employeeService.register(userForm);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/employee")
    public ResponseEntity<UserRegistrationResponseDTO> updateEmployee(@RequestBody @Valid EmployeeEditRequestDTO employeeEditRequest) {
        return ResponseEntity.ok(employeeService.edit(employeeEditRequest));
    }

    @GetMapping("/employee")
    public ResponseEntity<UserLoginResponseDTO> viewEmployee(@RequestBody @NotBlank String uid) {
        return ResponseEntity.ok(employeeService.view(uid.trim()));
    }

    @DeleteMapping("/employee")
    public ResponseEntity<String> deleteEmployee(@RequestBody @NotBlank String uid) {
        return ResponseEntity.ok(employeeService.delete(uid.trim()));
    }

    @PostMapping("/client")
    public ResponseEntity<UserRegistrationResponseDTO> registerClient(@RequestBody @Valid ClientRegistrationRequestDTO user) {
        return ResponseEntity.ok(clientService.register(user));
    }

    @PutMapping("/client")
    public ResponseEntity<UserRegistrationResponseDTO> updateClient(@RequestBody @Valid ClientEditRequestDTO clientEditRequest) {
        return ResponseEntity.ok(clientService.edit(clientEditRequest));
    }

    @GetMapping("/client")
    public ResponseEntity<UserLoginResponseDTO> viewClient(@RequestBody @NotBlank String uid) {
        return ResponseEntity.ok(clientService.view(uid.trim()));
    }

    @DeleteMapping("/client")
    public ResponseEntity<String> deleteClient(@RequestBody @NotBlank String uid) {
        return ResponseEntity.ok(clientService.delete(uid.trim()));
    }

    @PostMapping("/signin")
    public ResponseEntity<UserRegistrationResponseDTO> login(@RequestBody UserLoginRequestDTO loginRequest) throws TokenExpiredException {
        UserRegistrationResponseDTO responseDTO = userService.getUserLoginResponse(loginRequest);
        return ResponseEntity.ok(responseDTO);
    }

}
