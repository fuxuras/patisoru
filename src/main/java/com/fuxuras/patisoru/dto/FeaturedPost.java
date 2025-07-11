package com.fuxuras.patisoru.dto;

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
    private Long likeCount;
    private UserInFeaturedPost user;
   private LocalDateTime createdAt;
}
