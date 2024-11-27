package com.streamcommerce.service;

import com.streamcommerce.model.AuthUser;
import com.streamcommerce.model.UserPrincipal;
import com.streamcommerce.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    private final AuthUserRepository authUserRepository;

    @Autowired
    public AuthUserDetailsService(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = authUserRepository.findByUsername(username);

        if (authUser == null) {
            throw new UsernameNotFoundException("User with name \"" + username + "\" was not found");
        }

        return new UserPrincipal(authUser);
    }
}
