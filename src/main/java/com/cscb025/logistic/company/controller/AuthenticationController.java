package com.cscb025.logistic.company.controller;

import com.cscb025.logistic.company.controller.request.JWT.JwtRequest;
import com.cscb025.logistic.company.controller.request.user.EmployeeRegistrationRequestDTO;
import com.cscb025.logistic.company.controller.response.JWT.JwtResponse;
import com.cscb025.logistic.company.enums.EmployeeRole;
import com.cscb025.logistic.company.enums.UserRole;
import com.cscb025.logistic.company.service.JwtUserDetailsService;
import com.cscb025.logistic.company.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Used to verify that jwt returns to client jwt (json web token)
 */
@Controller
@CrossOrigin
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService userDetailsService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, JwtUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/chooseRole")
    public String chooseRole(Model model) {
        List<String> roles = new ArrayList<>(Arrays.asList(UserRole.CLIENT.toString().toUpperCase(),
                EmployeeRole.SUPPLIER.toString().toUpperCase(),
                EmployeeRole.OFFICE_WORKER.toString().toUpperCase()));
        model.addAttribute("roles", roles.toArray());
        return "chooseRole";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

    @GetMapping("/employeeContent")
    public String getEmployeeBody() {
        return "fragments :: employeeBody";
    }

    @GetMapping("/clientContent")
    public String getClientBody() {
        return "fragments :: employeeBody";
    }

    @GetMapping("/registration")
    public String registration(Model model, @RequestParam String role) {
        EmployeeRegistrationRequestDTO dto = new EmployeeRegistrationRequestDTO();
        dto.setEmployeeRole(role);
        model.addAttribute("userForm", dto);

        return "registrationEmployee";
    }

    @GetMapping(value = "/signin")
    public String login() {
        return "login";
    }

    /**
     * Get the username password from the client and use the secret key to encrypt it into json web token
     */
    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getEmail());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    /**
     * Get the username password from the client and use the secret key to encrypt it into json web token
     */
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    private void eraseAuthentication() {

        //TODO logout
    }

}
