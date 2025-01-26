package com.fuxuras.patisoru.controllers;

import com.fuxuras.patisoru.dto.RegisterRequest;
import com.fuxuras.patisoru.dto.ResponseMessage;
import com.fuxuras.patisoru.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "/auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "/auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequest reqisterRequest, RedirectAttributes redirectAttributes) {
        ResponseMessage message = userService.createUser(reqisterRequest);
        redirectAttributes.addFlashAttribute("message", message);
        if (message.getCode() > 0){
            return "redirect:/login";
        }else {
            return "redirect:/register";
        }
    }
}
