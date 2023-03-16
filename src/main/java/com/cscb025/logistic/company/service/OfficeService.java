package com.cscb025.logistic.company.service;

import com.cscb025.logistic.company.controller.request.admin.EditOfficeRequestDTO;
import com.cscb025.logistic.company.controller.request.admin.OfficeRequestDTO;
import com.cscb025.logistic.company.controller.response.admin.OfficeResponseDTO;
import com.cscb025.logistic.company.entity.Company;
import com.cscb025.logistic.company.entity.Office;
import com.cscb025.logistic.company.exception.EntityExistsException;
import com.cscb025.logistic.company.exception.EntityNotFoundException;
import com.cscb025.logistic.company.repository.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OfficeService {
    private final OfficeRepository officeRepository;
    private final CompanyService companyService;

    @Autowired
    public OfficeService(OfficeRepository officeRepository, CompanyService companyService) {
        this.officeRepository = officeRepository;
        this.companyService = companyService;
    }

    public OfficeResponseDTO view(String companyId) {
        Office office = getOffice(companyId);
        return new OfficeResponseDTO(office.getUid(), office.getName(), office.getAddress(), office.getPhone(),
                office.getCompany().getName());
    }

    public OfficeResponseDTO register(OfficeRequestDTO officeRequest) {
        checkOfficeNameExists(officeRequest.getName());
        Company company = companyService.getCompany(officeRequest.getCompanyId());

        Office office = new Office(officeRequest.getName(), officeRequest.getAddress(), officeRequest.getPhone(),
                company);
        office = officeRepository.save(office);
        return new OfficeResponseDTO(office.getUid(), office.getName(), office.getAddress(), office.getPhone(),
                office.getCompany().getName());
    }

    private void checkOfficeNameExists(String name) {
        if (officeRepository.findByName(name).isPresent()) {
            throw new EntityExistsException("Office with that name exists!");
        }
    }

    Office getOffice(String officeId) {
        Optional<Office> office = officeRepository.findById(officeId);
        if (!office.isPresent()) {
            throw new EntityNotFoundException("No such office found!");
        }
        return office.get();
    }

    public OfficeResponseDTO edit(EditOfficeRequestDTO officeRequest) {
        checkOfficeNameExists(officeRequest.getName());

        Office office = getOffice(officeRequest.getUid());
        office.setAddress(officeRequest.getAddress());
        office.setName(officeRequest.getName());
        office.setPhone(officeRequest.getPhone());

        office = officeRepository.save(office);
        return new OfficeResponseDTO(office.getUid(), office.getName(), office.getAddress(), office.getPhone(),
                office.getCompany().getName());
    }

    public String deleteOffice(String officeId) {
        Office office = getOffice(officeId);

        officeRepository.delete(office);
        return "Office was deleted!";
    }
}
