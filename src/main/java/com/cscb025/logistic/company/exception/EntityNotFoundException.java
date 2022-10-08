package com.cscb025.logistic.company.exception;

public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 8299010620642337631L;

    public EntityNotFoundException(String message) {
        super(message);
    }
}
