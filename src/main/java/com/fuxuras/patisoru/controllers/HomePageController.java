package com.fuxuras.patisoru.controllers;

import com.fuxuras.patisoru.dto.FeaturedPost;
import com.fuxuras.patisoru.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomePageController {

    private final PostService postService;

    @GetMapping("/")
    public String homepage(Model model) {
        List<FeaturedPost> featuredPosts = postService.getFeaturedPosts();
        model.addAttribute("featuredPosts", featuredPosts);
        return "homepage/homepage";
    }

    //TODO: feature: create feed/discover page
}
