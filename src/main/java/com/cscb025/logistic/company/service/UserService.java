package com.cscb025.logistic.company.service;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;

import com.cscb025.logistic.company.config.JwtTokenUtil;
import com.cscb025.logistic.company.controller.request.user.UserLoginRequestDTO;
import com.cscb025.logistic.company.controller.response.user.UserLoginResponseDTO;
import com.cscb025.logistic.company.controller.response.user.UserRegistrationResponseDTO;
import com.cscb025.logistic.company.entity.Client;
import com.cscb025.logistic.company.entity.Employee;
import com.cscb025.logistic.company.entity.User;
import com.cscb025.logistic.company.exception.TokenExpiredException;
import com.cscb025.logistic.company.repository.ClientRepository;
import com.cscb025.logistic.company.repository.EmployeeRepository;

import java.util.Optional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public record UserService(ClientRepository clientRepository, EmployeeRepository employeeRepository,
                          JwtTokenUtil jwtTokenUtil, PasswordEncoder encoder,
                          JwtUserDetailsService jwtUserDetailsService) {

    private static final String USER_NOT_FOUND = "User not found!";

    private static final String WRONG_CREDENTIALS = "Wrong credentials";

    private static final String TOKEN_EXPIRED = "Token has expired";

    public User findByEmail(String email) {
        User user;
        Optional<Employee> employeeOptional = employeeRepository.findByEmail(email);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            user = employee;
            user.setUid(employee.getUid());
            user.setEmail(employee.getEmail());
            user.setPassword(employee.getPassword());
        } else {
            Optional<Client> clientOptional = clientRepository.findByEmail(email);
            if (clientOptional.isPresent()) {
                Client client = clientOptional.get();
                user = client;
                user.setUid(client.getUid());
                user.setEmail(client.getEmail());
                user.setPassword(client.getPassword());
            } else {
                throw new BadCredentialsException("Wrong credentials");
            }
        }
        return user;
    }

    public UserRegistrationResponseDTO getUserLoginResponse(
            UserLoginRequestDTO authenticationRequest) throws TokenExpiredException {
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        User user = findByEmail(authenticationRequest.getEmail());

        try {
            authenticate(authenticationRequest, user);
        }
        catch (ExpiredJwtException e) {
            throw new TokenExpiredException(TOKEN_EXPIRED);
        }

        UserLoginResponseDTO userLogin = new UserLoginResponseDTO(user.getUid(), user.getEmail(), "name",
                user.getUserRole(), "officeID");
        return new UserRegistrationResponseDTO(jwtTokenUtil.generateToken(userDetails), userLogin);
    }

    private void authenticate(UserLoginRequestDTO user, User user1) {
        if (!encoder.matches(user.getPassword(), user1.getPassword())) {
            throw new BadCredentialsException(WRONG_CREDENTIALS);
        }
    }
}
