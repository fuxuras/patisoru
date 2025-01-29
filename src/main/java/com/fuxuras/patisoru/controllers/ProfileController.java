package com.fuxuras.patisoru.controllers;

import com.fuxuras.patisoru.dto.UserResponse;
import com.fuxuras.patisoru.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final UserService userService;

    @GetMapping
    public String profile(Model model, Principal principal) {
        UserResponse user = userService.getByEmail(principal.getName());
        model.addAttribute("user", user);
        return "profile/profile";
    }

}
