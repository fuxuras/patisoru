package com.fuxuras.patisoru.controllers;

import com.fuxuras.patisoru.dto.PostResponse;
import com.fuxuras.patisoru.repositories.PostRepository;
import com.fuxuras.patisoru.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
