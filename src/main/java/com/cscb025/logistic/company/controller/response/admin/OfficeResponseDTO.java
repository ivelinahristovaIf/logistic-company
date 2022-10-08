package com.cscb025.logistic.company.controller.response.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfficeResponseDTO {

    private String uid;
    private String name;
    private String address;
    private String phone;
    private String companyName;

}
