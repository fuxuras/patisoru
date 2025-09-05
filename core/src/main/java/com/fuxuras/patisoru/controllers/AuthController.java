package com.fuxuras.patisoru.controllers;

import com.fuxuras.patisoru.dto.EmailVerification;
import com.fuxuras.patisoru.dto.RegisterRequest;
import com.fuxuras.patisoru.dto.auth.LoginRequest;
import com.fuxuras.patisoru.dto.auth.LoginResponse;
import com.fuxuras.patisoru.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest registerRequest) {
        authenticationService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        LoginResponse loginResponse = authenticationService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/email-verification")
    public ResponseEntity<Void> emailVerification(@Valid @RequestBody EmailVerification emailVerification) {
        authenticationService.verifyUser(emailVerification);
        return ResponseEntity.ok().build();
    }
}
