package com.fuxuras.patisoru.controllers;

import com.fuxuras.patisoru.dto.ResponseMessage;
import com.fuxuras.patisoru.dto.UserEditRequest;
import com.fuxuras.patisoru.dto.UserResponse;
import com.fuxuras.patisoru.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/edit")
    public String edit(Model model, Principal principal) {
        UserResponse user = userService.getByEmail(principal.getName());
        model.addAttribute("user", user);
        return "profile/profile-edit";
    }

    @PostMapping("/edit")
    public String update(Principal principal, @ModelAttribute UserEditRequest userEditRequest, RedirectAttributes redirectAttributes){
        ResponseMessage message = userService.editUser(userEditRequest, principal.getName());
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/profile";
    }
}
