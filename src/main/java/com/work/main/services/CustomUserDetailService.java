package com.work.main.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.work.main.entities.User;
import com.work.main.models.CustomUserDetails;

@Component
public class CustomUserDetailService implements UserDetailsService {

    private UserService userService;

    @Autowired
    public CustomUserDetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userService
                .findByNameAndDeletedFalse(name)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("[CustomUserDetailService] User with name %s not found", name)));

        return new CustomUserDetails(user);
    }
}
