package com.fuxuras.patisoru.controllers;

import com.fuxuras.patisoru.dto.RegisterRequest;
import com.fuxuras.patisoru.dto.ResponseMessage;
import com.fuxuras.patisoru.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        // TODO: refactor: flash attribute when login redirect
        // this is obviously not the best way but i cant find any other way
        if(request.getSession().getAttribute("message") != null) {
            ResponseMessage responseMessage = (ResponseMessage) request.getSession().getAttribute("message");
            request.getSession().removeAttribute("message");
            model.addAttribute("message", responseMessage);
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
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
