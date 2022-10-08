package com.cscb025.logistic.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private static final String ROLE_PREFIX = "ROLE_";

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        com.cscb025.logistic.company.entity.User user = userService.findByEmail(username);

        return new User(user.getEmail(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(ROLE_PREFIX + user.getUserRole().name())));
    }
}
