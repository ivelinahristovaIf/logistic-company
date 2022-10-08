package com.cscb025.logistic.company.controller.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequestDTO {

    @NotBlank(message = "Email must not be empty!")
    @Pattern(regexp = ".+@.+\\.[a-z]+", message = "Invalid email address format!")
    private String email;

    @NotBlank(message = "Password must not be empty!")
    private String password;

}
