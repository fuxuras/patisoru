package com.fuxuras.patisoru.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentInPostResponse {
    private UUID id;
    private Date createdAt;
    private String text;
    private UserInPostResponse user;
}
