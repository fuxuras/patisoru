package com.fuxuras.patisoru.controllers;

import com.fuxuras.patisoru.dto.CommentCreateRequest;
import com.fuxuras.patisoru.dto.ResponseMessage;
import com.fuxuras.patisoru.entities.Comment;
import com.fuxuras.patisoru.services.CommentService;
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
@RequestMapping("/post/{id}/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create")
    public String createComment(@Valid @ModelAttribute CommentCreateRequest commentCreateRequest, 
                               BindingResult bindingResult,
                               Principal principal, 
                               @PathVariable(name = "id") UUID postId, 
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // Create error message
            ResponseMessage errorMessage = new ResponseMessage();
            errorMessage.setMessage("Yorum oluşturulurken bir hata oluştu. Lütfen girdiğiniz bilgileri kontrol edin.");
            errorMessage.setCode(-1);
            redirectAttributes.addFlashAttribute("message", errorMessage);
            return "redirect:/post/" + postId;
        }
        
        ResponseMessage message = commentService.create(commentCreateRequest, principal.getName(), postId);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/post/" + postId;
    }

    //TODO: feature: comment delete

    //TODO: feature: comment update

}
