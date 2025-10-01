package com.fuxuras.patisoru.dto.comment;

import com.fuxuras.patisoru.dto.user.UserSummaryResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private UUID id;
    private LocalDateTime createdAt;
    private String text;
    private UserSummaryResponse user;
}
