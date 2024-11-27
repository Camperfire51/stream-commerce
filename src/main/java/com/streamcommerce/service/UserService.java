package com.streamcommerce.service;

import com.streamcommerce.model.AuthUser;
import com.streamcommerce.model.User;
import com.streamcommerce.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final JWTService jwtService;
    private final AuthenticationManager authManager;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    public UserService(JWTService jwtService, AuthenticationManager authManager) {
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    public User register(AuthUser authUser) {
        return null;
    }

    public String verify(AuthUser authUser) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(authUser.getUsername(), authUser.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authUser.getUsername());
        } else {
            return "fail";
        }
    }


}
