package com.fuxuras.patisoru.controllers;

import com.fuxuras.patisoru.dto.EmailVerification;
import com.fuxuras.patisoru.dto.RegisterRequest;
import com.fuxuras.patisoru.dto.ResponseMessage;
import com.fuxuras.patisoru.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> register(@Valid @RequestBody RegisterRequest registerRequest) {
        ResponseMessage message = userService.createUser(registerRequest);
        if (message.getCode() > 0) {
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }

    @PostMapping("/email-verification")
    public ResponseEntity<ResponseMessage> emailVerification(@RequestBody EmailVerification emailVerification) {
        ResponseMessage message = userService.verifyUser(emailVerification);
        if (message.getCode() > 0) {
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }
}
