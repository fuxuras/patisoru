package com.fuxuras.patisoru.controllers;

import com.fuxuras.patisoru.dto.EmailVerification;
import com.fuxuras.patisoru.dto.RegisterRequest;
import com.fuxuras.patisoru.dto.ResponseMessage;
import com.fuxuras.patisoru.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.Message;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerRequest") RegisterRequest registerRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        ResponseMessage message = userService.createUser(registerRequest);
        redirectAttributes.addFlashAttribute("message", message);
        if (message.getCode() > 0){
            return "redirect:/login";
        }else {
            return "redirect:/register";
        }
    }

    @GetMapping("/email-verification")
    public String emailVerification(@RequestParam(value = "email", required = false) String email, @RequestParam(value = "code", required = false) String code, RedirectAttributes redirectAttributes) {
        ResponseMessage message = new ResponseMessage();
        if (code != null && !code.isEmpty() && email != null && !email.isEmpty()) {
            EmailVerification emailVerification = new EmailVerification();
            emailVerification.setEmail(email);
            emailVerification.setToken(code);
            message = userService.verifyUser(emailVerification);
        }else {
            message.setMessage("Email verification failed");
            message.setCode(-1);
        }
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/login";
    }
}
