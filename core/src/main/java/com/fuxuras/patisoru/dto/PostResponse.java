package com.fuxuras.patisoru.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private UUID id;
    private LocalDateTime createdAt;
    private String title;
    private String text;
    private UserInPostResponse user;
    private List<CommentInPostResponse> comments;
    private long likeCount;
    private String userLikeStatus;
}
