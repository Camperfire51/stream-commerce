package com.streamcommerce.controller;

import com.streamcommerce.model.AuthUser;
import com.streamcommerce.model.User;
import com.streamcommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody AuthUser user) {
        return userService.register(user);
    }

}
