package com.fuxuras.patisoru.controllers;

import com.fuxuras.patisoru.dto.ResponseMessage;
import com.fuxuras.patisoru.dto.user.UserEditRequest;
import com.fuxuras.patisoru.dto.user.UserResponse;
import com.fuxuras.patisoru.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponse> profile(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponse user = userService.getByEmail(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<ResponseMessage> update(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody UserEditRequest userEditRequest) {
        ResponseMessage message = userService.editUser(userEditRequest, userDetails.getUsername());
        if (message.getCode() > 0) {
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }
}
