package com.fuxuras.patisoru.dto;

import com.fuxuras.patisoru.dto.user.UserSummaryResponse;
import com.fuxuras.patisoru.entities.PostType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeaturedPost{
    private UUID id;
    private String title;
    private String text;
    private PostType postType;
    private Long likeCount;
    private UserSummaryResponse user;
   private LocalDateTime createdAt;
}
