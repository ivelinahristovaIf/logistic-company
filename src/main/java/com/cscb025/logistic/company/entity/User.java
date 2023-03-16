package com.cscb025.logistic.company.entity;

import com.cscb025.logistic.company.enums.UserRole;
import lombok.Data;

@Data
public abstract class User {

    private String uid;

    private String email;

    private String password;

    private UserRole userRole;

    public String getUid() {
        if (this instanceof Employee) {
            Employee employee = (Employee) this;
            return employee.getUid();
        } else if (this instanceof Client) {
            Client client = (Client) this;
            return client.getUid();
        }
        return this.uid;
    }

    public String getEmail() {
        if (this instanceof Employee) {
            Employee employee = (Employee) this;
            return employee.getEmail();
        } else if (this instanceof Client) {
            Client client = (Client) this;
            return client.getEmail();
        }
        return this.email;
    }

    public String getPassword() {
        if (this instanceof Employee) {
            Employee employee = (Employee) this;
            return employee.getPassword();
        } else if (this instanceof Client) {
            Client client = (Client) this;
            return client.getPassword();
        }
        return this.password;
    }

    public UserRole getUserRole() {
        if (this instanceof Employee) {
            Employee employee = (Employee) this;
            if (employee.getRole().name().equalsIgnoreCase(UserRole.ADMIN.name())) {
                return UserRole.ADMIN;
            }
            return UserRole.EMPLOYEE;
        } else if (this instanceof Client) {
            return UserRole.CLIENT;
        }
        return this.userRole;
    }

}
