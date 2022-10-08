package com.cscb025.logistic.company.service;

import com.cscb025.logistic.company.controller.request.admin.CompanyRequestDTO;
import com.cscb025.logistic.company.controller.response.admin.CompanyResponseDTO;
import com.cscb025.logistic.company.entity.Company;
import com.cscb025.logistic.company.exception.EntityExistsException;
import com.cscb025.logistic.company.exception.EntityNotFoundException;
import com.cscb025.logistic.company.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public CompanyResponseDTO view(String companyId) {
        Company company = getCompany(companyId);
        return new CompanyResponseDTO(company.getUid(), company.getName());
    }

    public CompanyResponseDTO register(String name) {
        checkCompanyNameExists(name);
        Company company = new Company(name);
        company = companyRepository.save(company);
        return new CompanyResponseDTO(company.getUid(), company.getName());
    }

    public CompanyResponseDTO edit(CompanyRequestDTO companyRequestDTO) {
        checkCompanyNameExists(companyRequestDTO.getName());

        Company company = getCompany(companyRequestDTO.getUid());
        company.setName(companyRequestDTO.getName());

        company = companyRepository.save(company);
        return new CompanyResponseDTO(company.getUid(), company.getName());
    }

    Company getCompany(String companyId) {
        Optional<Company> companyOptional = companyRepository.findById(companyId);
        if (!companyOptional.isPresent()) {
            throw new EntityNotFoundException("No such company found!");
        }
        return companyOptional.get();
    }

    private void checkCompanyNameExists(String name) {
        if (companyRepository.findByName(name).isPresent()) {
            throw new EntityExistsException("Company with that name exists!");
        }
    }

    public String delete(String companyId) {
        Company company = getCompany(companyId);

        companyRepository.delete(company);
        return "Company was deleted!";
    }
}
