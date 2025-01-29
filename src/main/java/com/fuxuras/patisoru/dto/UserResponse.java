package com.fuxuras.patisoru.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private LocalDate createdAt;
    private String fullName;
    private String email;
}
