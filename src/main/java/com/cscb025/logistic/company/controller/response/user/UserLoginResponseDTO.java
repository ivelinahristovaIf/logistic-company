package com.cscb025.logistic.company.controller.response.user;

import com.cscb025.logistic.company.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponseDTO {

    private String uid;
    private String email;
    private String name;
    private UserRole userRole;
    private String relativeId;

}
