package com.fuxuras.patisoru.dto.post;

import com.fuxuras.patisoru.dto.CommentInPostResponse;
import com.fuxuras.patisoru.dto.user.UserSummaryResponse;
import com.fuxuras.patisoru.entities.PostType;
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
    private PostType postType;
    private UserSummaryResponse user;
    private List<CommentInPostResponse> comments;
    private long likeCount;
    private String userLikeStatus;
}
