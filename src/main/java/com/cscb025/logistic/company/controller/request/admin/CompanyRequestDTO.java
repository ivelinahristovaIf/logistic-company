package com.cscb025.logistic.company.controller.request.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequestDTO {

    @NotBlank
    private String uid;

    @NotBlank
    private String name;

}
