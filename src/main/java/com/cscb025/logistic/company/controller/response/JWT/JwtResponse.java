package com.cscb025.logistic.company.controller.response.JWT;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

@Data
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;

    @NonNull
    private final String jwttoken;

}