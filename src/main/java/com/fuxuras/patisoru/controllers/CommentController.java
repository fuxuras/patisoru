package com.fuxuras.patisoru.controllers;

import com.fuxuras.patisoru.dto.CommentCreateRequest;
import com.fuxuras.patisoru.dto.ResponseMessage;
import com.fuxuras.patisoru.entities.Comment;
import com.fuxuras.patisoru.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post/{id}/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create")
    public String createComment(@ModelAttribute CommentCreateRequest commentCreateRequest, Principal principal, @PathVariable(name = "id") UUID postId, RedirectAttributes redirectAttributes) {
        ResponseMessage message = commentService.create(commentCreateRequest,principal.getName(),postId);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/post/" + postId;
    }

}
