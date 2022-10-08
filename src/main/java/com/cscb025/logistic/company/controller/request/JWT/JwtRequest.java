package com.cscb025.logistic.company.controller.request.JWT;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

@Data
public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    @NonNull
    private String email;
    @NonNull
    private String password;

}
