package com.cscb025.logistic.company.controller.request.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfficeRequestDTO {
    private String address;
    private String name;
    private String phone;
    private String companyId;
}
