package com.cscb025.logistic.company.controller.response;

import com.cscb025.logistic.company.enums.EmployeeRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {

    private String email;
    private String name;
    private EmployeeRole role;
    private String officeName;

}
