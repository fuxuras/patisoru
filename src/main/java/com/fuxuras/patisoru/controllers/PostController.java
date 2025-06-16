package com.fuxuras.patisoru.controllers;

import com.fuxuras.patisoru.dto.PostCreateRequest;
import com.fuxuras.patisoru.dto.PostResponse;
import com.fuxuras.patisoru.dto.ResponseMessage;
import com.fuxuras.patisoru.services.LikeService;
import com.fuxuras.patisoru.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final LikeService likeService;

    @GetMapping("/{id}")
    public String post(@PathVariable UUID id, Model model, Principal principal) {
        PostResponse post = postService.getPostById(id);
        model.addAttribute("post", post);
        String userLikeStatus;
        if (principal == null) {
            userLikeStatus = "remove";
        }else{
            userLikeStatus = likeService.getStatus(post.getId(), principal.getName());
        }
        model.addAttribute("userLikeStatus", userLikeStatus);
        return "posts/single-post";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("post", new PostCreateRequest());
        return "posts/post-form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute PostCreateRequest post, BindingResult bindingResult, Principal principal, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            return "posts/post-form";
        }
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
