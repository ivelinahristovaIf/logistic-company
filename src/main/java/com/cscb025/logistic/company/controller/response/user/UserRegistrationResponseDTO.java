package com.cscb025.logistic.company.controller.response.user;

import com.cscb025.logistic.company.controller.response.user.UserLoginResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationResponseDTO {

    private String token;

    private UserLoginResponseDTO userLoginResponse;

}
