package com.cscb025.logistic.company.controller.request.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditOfficeRequestDTO {
    @NotBlank
    private String uid;
    @NotBlank
    private String address;
    @NotBlank
    private String name;
    @Pattern(regexp = "(\\+)?(359|0)8[789]\\d{1}\\d{3}\\d{3}", message = "Invalid phone number format!")
    @NotBlank
    private String phone;
}
