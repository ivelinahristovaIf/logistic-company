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
public class EmployeeEditRequestDTO {

    @NotBlank(message = "Uid must not be empty!")
    private String uid;

    @NotBlank(message = "Email must not be empty!")
    @Pattern(regexp = ".+@.+\\.[a-z]+", message = "Invalid email address format!")
    private String email;

    @NotBlank(message = "Name must not be empty!")
    private String name;

    @NotBlank(message = "Password must not be empty!")
    @Size(min = 6, message = "Length must be at least 6 symbols!")
    private String password;

}
