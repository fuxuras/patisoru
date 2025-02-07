package com.fuxuras.patisoru.controllers;

import com.fuxuras.patisoru.dto.PostCreateRequest;
import com.fuxuras.patisoru.dto.PostResponse;
import com.fuxuras.patisoru.dto.ResponseMessage;
import com.fuxuras.patisoru.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @GetMapping("/{id}")
    public String post(@PathVariable UUID id, Model model) {
        PostResponse post = postService.getPostById(id);
        model.addAttribute("post", post);
        return "/posts/single-post";
    }

    @GetMapping("/create")
    public String create() {
        return "/posts/post-form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute PostCreateRequest post, Principal principal, RedirectAttributes redirectAttributes){
        ResponseMessage message = postService.create(post, principal.getName());
        redirectAttributes.addFlashAttribute("message", message);
        if (message.getCode() > 0){
            return "redirect:/";
        }else {
            return "redirect:/post/create";
        }
    }

    // TODO: feature: post update
    // TODO: feature: post delete
}
