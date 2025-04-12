package com.fuxuras.patisoru.controllers;

import com.fuxuras.patisoru.dto.FeaturedPost;
import com.fuxuras.patisoru.dto.PostResponse;
import com.fuxuras.patisoru.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/discover")
    public String discoverPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            Model model) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? 
                                      Sort.Direction.ASC : Sort.Direction.DESC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<PostResponse> posts = postService.getAllPosts(pageable);
        
        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        
        return "homepage/discover";
    }
}
