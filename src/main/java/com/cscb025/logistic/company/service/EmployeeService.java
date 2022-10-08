package com.cscb025.logistic.company.service;

import com.cscb025.logistic.company.controller.request.user.EmployeeEditRequestDTO;
import com.cscb025.logistic.company.controller.request.user.EmployeeRegistrationRequestDTO;
import com.cscb025.logistic.company.controller.response.EmployeeResponseDTO;
import com.cscb025.logistic.company.controller.response.user.UserLoginResponseDTO;
import com.cscb025.logistic.company.controller.response.user.UserRegistrationResponseDTO;
import com.cscb025.logistic.company.entity.Employee;
import com.cscb025.logistic.company.entity.Office;
import com.cscb025.logistic.company.enums.EmployeeRole;
import com.cscb025.logistic.company.enums.UserRole;
import com.cscb025.logistic.company.exception.EntityExistsException;
import com.cscb025.logistic.company.exception.EntityNotFoundException;
import com.cscb025.logistic.company.exception.InvalidRoleException;
import com.cscb025.logistic.company.exception.OfficeNotFoundException;
import com.cscb025.logistic.company.repository.EmployeeRepository;
import com.cscb025.logistic.company.repository.OfficeRepository;
import com.cscb025.logistic.company.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private static final String EMAIL_ALREADY_TAKEN = "Email, already taken!";
    private static final String NO_SUCH_OFFICE = "No such office in the system!";
    private static final String ADMIN_NOT_SUPPORTED = "ADMIN is not supported for registration!";
    private static final String INVALID_ROLE = "Invalid role for registration!";

    private final EmployeeRepository employeeRepository;
    private final OfficeRepository officeRepository;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder encoder;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, OfficeRepository officeRepository, JwtUserDetailsService jwtUserDetailsService, JwtTokenUtil jwtTokenUtil, PasswordEncoder encoder) {
        this.employeeRepository = employeeRepository;
        this.officeRepository = officeRepository;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.encoder = encoder;
    }

    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .filter(employee -> employee.getRole() != EmployeeRole.ADMIN)
                .map(employee -> new EmployeeResponseDTO(employee.getEmail(), employee.getName(), employee.getRole(), employee.getOffice().getName()))
                .collect(Collectors.toList());
    }

    public UserRegistrationResponseDTO register(EmployeeRegistrationRequestDTO user) {
        if (employeeRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EntityExistsException(EMAIL_ALREADY_TAKEN);
        }
        Optional<Office> office = officeRepository.findById(user.getOfficeId());
        if (!office.isPresent()) {
            throw new OfficeNotFoundException(NO_SUCH_OFFICE);
        }

        Employee employee = new Employee(user.getEmail(), encoder.encode(user.getPassword()), EmployeeRole.SUPPLIER);//Default supplier role
        employee.setName(user.getName());
        employee.setOffice(office.get());
        setRole(user, employee);
        employee = employeeRepository.save(employee);
        return getResponse(employee);
    }

    private UserRegistrationResponseDTO getResponse(Employee employee) {
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(employee.getEmail());
        UserLoginResponseDTO userLoginResponseDTO = new UserLoginResponseDTO(employee.getUid(), employee.getEmail(), employee.getName(), UserRole.EMPLOYEE, employee.getOffice().getUid());
        return new UserRegistrationResponseDTO(jwtTokenUtil.generateToken(userDetails), userLoginResponseDTO);
    }

    private void setRole(EmployeeRegistrationRequestDTO user, Employee employee) {
        switch (user.getEmployeeRole()) {
            case "SUPPLIER":
                break;
            case "OFFICE_WORKER":
                employee.setRole(EmployeeRole.OFFICE_WORKER);
                break;
            case "ADMIN":
                throw new InvalidRoleException(ADMIN_NOT_SUPPORTED);
            default:
                throw new InvalidRoleException(INVALID_ROLE);
        }
    }

    public UserRegistrationResponseDTO edit(EmployeeEditRequestDTO employeeEditRequest) {
        Employee employee = getEmployee(employeeEditRequest.getUid());

        employee.setName(employeeEditRequest.getName());
        employee.setEmail(employeeEditRequest.getEmail());
        employee.setPassword(employeeEditRequest.getPassword());
        employeeRepository.save(employee);

        return getResponse(employee);
    }

    public String delete(String employeeId) {
        Employee employee = getEmployee(employeeId);

        employeeRepository.delete(employee);
        return "Employee was deleted!";
    }

    public UserLoginResponseDTO view(String uid) {
        Employee employee = getEmployee(uid);
        return new UserLoginResponseDTO(employee.getUid(), employee.getEmail(), employee.getName(), UserRole.EMPLOYEE, employee.getOffice().getUid());
        //TODO add employee role and office name
    }

    Employee getEmployee(String uid) {
        Optional<Employee> employeeOptional = employeeRepository.findById(uid);
        if (!employeeOptional.isPresent()) {
            throw new EntityNotFoundException("No such user in the system!");
        }
        return employeeOptional.get();
    }


}
