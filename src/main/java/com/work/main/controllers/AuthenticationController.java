package com.work.main.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.work.main.entities.User;
import com.work.main.filters.TokenProvider;
import com.work.main.models.AuthenticationRequest;
import com.work.main.models.AuthenticationResponse;
import com.work.main.services.UserService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins="https://stark-coast-58195.herokuapp.com") 
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    
    private UserService userService;

    private TokenProvider tokenProvider;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, TokenProvider tokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @PostMapping()
    @ApiOperation(value = "obtiene el token de autenticacion")
    public ResponseEntity authenticate(@RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUserName(),
                        authenticationRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Optional<User> chale = userService.getUserByName(authenticationRequest.getUserName());
        boolean isAdmin = chale.get().isAdmin();
        
        return ResponseEntity.ok(new AuthenticationResponse(tokenProvider.generateToken(authentication), isAdmin));
    }

}