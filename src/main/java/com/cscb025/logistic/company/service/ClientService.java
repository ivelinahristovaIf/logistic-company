package com.cscb025.logistic.company.service;

import lombok.AllArgsConstructor;

import com.cscb025.logistic.company.config.JwtTokenUtil;
import com.cscb025.logistic.company.controller.request.user.ClientEditRequestDTO;
import com.cscb025.logistic.company.controller.request.user.ClientRegistrationRequestDTO;
import com.cscb025.logistic.company.controller.response.ClientResponseDTO;
import com.cscb025.logistic.company.controller.response.user.UserLoginResponseDTO;
import com.cscb025.logistic.company.controller.response.user.UserRegistrationResponseDTO;
import com.cscb025.logistic.company.entity.Client;
import com.cscb025.logistic.company.entity.Company;
import com.cscb025.logistic.company.enums.UserRole;
import com.cscb025.logistic.company.exception.EntityExistsException;
import com.cscb025.logistic.company.exception.EntityNotFoundException;
import com.cscb025.logistic.company.repository.ClientRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public record ClientService(ClientRepository clientRepository, CompanyService companyService, PasswordEncoder encoder,
                            JwtUserDetailsService jwtUserDetailsService, JwtTokenUtil jwtTokenUtil) {

    private static final String EMAIL_ALREADY_TAKEN = "Email is already taken!";

    public UserRegistrationResponseDTO register(ClientRegistrationRequestDTO user) {
        if (clientRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EntityExistsException(EMAIL_ALREADY_TAKEN);
        }
        Company company = companyService.getCompany(user.getCompanyName());

        Client client = new Client(user.getName(), user.getEmail(), encoder.encode(user.getPassword()), user.getPhone(),
                company);
        client = clientRepository.save(client);
        return getResponse(client);
    }

    private UserRegistrationResponseDTO getResponse(Client client) {
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(client.getEmail());
        UserLoginResponseDTO userLoginResponseDTO = new UserLoginResponseDTO(client.getUid(), client.getEmail(),
                client.getName(), UserRole.CLIENT, client.getCompany().getUid());
        return new UserRegistrationResponseDTO(jwtTokenUtil.generateToken(userDetails), userLoginResponseDTO);
    }

    public UserRegistrationResponseDTO edit(ClientEditRequestDTO editRequest) {
        Client client = getClient(editRequest.getUid());
        client.setName(editRequest.getName());
        client.setEmail(editRequest.getEmail());
        client.setPassword(editRequest.getPassword());
        client.setPhone(editRequest.getPhone());
        clientRepository.save(client);

        return getResponse(client);
    }

    public String delete(String employeeId) {
        Client employee = getClient(employeeId);
        clientRepository.delete(employee);
        return "Client was deleted!";
    }

    public UserLoginResponseDTO view(String uid) {
        Client client = getClient(uid);
        return new UserLoginResponseDTO(client.getUid(), client.getEmail(), client.getName(), UserRole.CLIENT,
                client.getCompany().getUid());
        //TODO add company name
    }

    Client getClient(String uid) {
        Optional<Client> client = clientRepository.findById(uid);
        if (client.isEmpty()) {
            throw new EntityNotFoundException("No such user in the system!");
        }
        return client.get();
    }

    public List<ClientResponseDTO> getAll() {
        return clientRepository
                .findAll()
                .stream()//TODO maybe add filter for company
                .map(client -> new ClientResponseDTO(client.getEmail(), client.getName(), client.getPhone()))
                .collect(Collectors.toList());
    }
}
