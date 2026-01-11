package com.support.auth_service.controller;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.support.auth_service.model.LoginRequest;
import com.support.auth_service.model.RegisterRequest;
import com.support.auth_service.model.User;
import com.support.auth_service.security.JwtService;
import com.support.auth_service.service.RegistrationService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RegistrationService registrationService;

    public AuthController(AuthenticationManager authenticationManager,
            JwtService jwtService, RegistrationService registrationService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.registrationService = registrationService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // If authentication fails → exception is thrown automatically
        User user = registrationService.findByEmail(request.getEmail());
        return jwtService.generateToken(user);
        
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/hello")
public String hello(Authentication auth) {
    System.out.println("Authentication object: ");
    if (auth == null) {
        return "Unauthenticated";
    }
    return "Hello " + auth.getName();
}


    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {

        User user = registrationService.register(
                request.getEmail(),
                request.getPassword(),
                request.getName()
        );

        return user;
    }

}
