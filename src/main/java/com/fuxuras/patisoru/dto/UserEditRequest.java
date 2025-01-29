package com.fuxuras.patisoru.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEditRequest {
    private String email;
    private String fullName;
}
