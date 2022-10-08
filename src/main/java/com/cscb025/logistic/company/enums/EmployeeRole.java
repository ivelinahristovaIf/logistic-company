package com.cscb025.logistic.company.enums;

public enum EmployeeRole {
    ADMIN("Admin"), OFFICE_WORKER("Office worker"), SUPPLIER("Supplier");

    private String name;

    EmployeeRole(String name) {
        this.name = name;
    }
}
