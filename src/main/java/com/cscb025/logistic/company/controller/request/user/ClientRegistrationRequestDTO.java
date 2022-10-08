package com.cscb025.logistic.company.controller.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientRegistrationRequestDTO {
    @NotBlank(message = "Email must not be empty!")
    @Pattern(regexp = ".+@.+\\.[a-z]+", message = "Invalid email address format!")
    private String email;

    @NotBlank(message = "Name must not be empty!")
    private String name;

    @NotBlank(message = "Password must not be empty!")
    @Size(min = 6, message = "Length must be at least 6 symbols!")
    private String password;

    @NotBlank(message = "Password must not be empty!")
    @Size(min = 6, message = "Length must be at least 6 symbols!")
    private String repeatPassword;

    @Pattern(regexp = "(\\+)?(359|0)8[789]\\d{1}\\d{3}\\d{3}", message = "Invalid phone number format!")
    private String phone;

    @NotBlank(message = "Company must not be empty!")
    private String companyName;
}
