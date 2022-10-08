package com.cscb025.logistic.company.util;

import com.cscb025.logistic.company.entity.Company;
import com.cscb025.logistic.company.entity.Employee;
import com.cscb025.logistic.company.entity.Office;
import com.cscb025.logistic.company.enums.EmployeeRole;
import com.cscb025.logistic.company.repository.CompanyRepository;
import com.cscb025.logistic.company.repository.EmployeeRepository;
import com.cscb025.logistic.company.repository.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

@ConditionalOnProperty(value = "basic.inserts.enable", havingValue = "true")
@Component
public class BasicInserts {

    private static final String NBU_LOGISTICS = "NBU Logistics";

    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;
    private final OfficeRepository officeRepository;
    private final PasswordEncoder encoder;


    @Autowired
    public BasicInserts(CompanyRepository companyRepository, EmployeeRepository employeeRepository, OfficeRepository officeRepository, PasswordEncoder encoder) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
        this.officeRepository = officeRepository;
        this.encoder = encoder;
    }

    @PostConstruct
    public void basicInserts() {
        Company nbu = new Company(NBU_LOGISTICS);
        nbu = saveCompany(nbu);

        Employee admin = new Employee("admin@nbu.bg", "password", EmployeeRole.ADMIN);
        admin = saveEmployee(admin);

        Office office = new Office("NBU Logistics Office", "Montevideo 21, Sofia", "0898130130", nbu);
        office = saveOffice(office);

    }

    private Company saveCompany(Company company) {
        Optional<Company> company1 = companyRepository.findByName(company.getName());
        if (!company1.isPresent()) {
            company = companyRepository.save(company);
            return company;
        }
        return company1.get();
    }

    private Employee saveEmployee(Employee employee) {
        Optional<Employee> employeeOptional = employeeRepository.findByEmail(employee.getEmail());
        if (!employeeOptional.isPresent()) {
            employee.setPassword(encoder.encode(employee.getPassword()));
            employee = employeeRepository.save(employee);
            return employee;
        }
        return employeeOptional.get();
    }

    private Office saveOffice(Office office) {
        Optional<Office> officeOptional = officeRepository.findByName(office.getName());
        if (!officeOptional.isPresent()) {
            office = officeRepository.save(office);
            return office;
        }
        return officeOptional.get();
    }
}
